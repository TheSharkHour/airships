package com.shark.airships.entity;

import com.shark.airships.Airships;
import com.shark.airships.events.EntityListener;
import com.shark.airships.events.ItemListener;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.entity.projectile.thrown.EggEntity;
import net.minecraft.entity.projectile.thrown.SnowballEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.DyeItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.modificationstation.stationapi.api.recipe.FuelRegistry;
import net.modificationstation.stationapi.api.server.entity.EntitySpawnDataProvider;
import net.modificationstation.stationapi.api.server.entity.HasTrackingParameters;
import net.modificationstation.stationapi.api.util.Identifier;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;

/**
 * @author TheSharkHour
 * @since 01/25/2026
 * <p>Entity class for the Airship.</p>
 */
@HasTrackingParameters(trackingDistance = 160, updatePeriod = 2)
public class AirshipEntity extends Entity implements Inventory, EntitySpawnDataProvider {
    // Balloon coloring/texturing
    public static final int DATA_BALLOON_COLOR = 0b0001_0000;
    public static final float[][] COLORS = new float[][]{{1.0F, 1.0F, 1.0F}, {0.95F, 0.7F, 0.2F}, {0.9F, 0.5F, 0.85F}, {0.6F, 0.7F, 0.95F}, {0.9F, 0.9F, 0.2F}, {0.5F, 0.8F, 0.1F}, {0.95F, 0.7F, 0.8F}, {0.3F, 0.3F, 0.3F}, {0.6F, 0.6F, 0.6F}, {0.3F, 0.6F, 0.7F}, {0.7F, 0.4F, 0.9F}, {0.2F, 0.4F, 0.8F}, {0.5F, 0.4F, 0.3F}, {0.4F, 0.5F, 0.2F}, {0.8F, 0.3F, 0.3F}, {0.1F, 0.1F, 0.1F}};

    public static final int DATA_HURT = 17;
    public static final int DATA_HURT_DIR = 18;
    public static final int DATA_DAMAGE = 19;

    // Mod compatibility
    private static final boolean ELEMENTAL_ARROWS = FabricLoader.getInstance().isModLoaded("elementalarrows");
    private static final boolean WOLVES = FabricLoader.getInstance().isModLoaded("wolves");

    // Checks for the client (player) pressing controls
    public boolean clientPressingUp = false;
    public boolean clientPressingDown = false;
    public boolean clientPressingFire = false;

    // Fuel times and boost
    public int litTime = 0;
    public int litDuration = 0;
    public boolean boost = false;

    // Arrow counter and whether they've been fired
    private int arrowCounter;
    boolean hasFired;

    // Airship Inventory
    private final ItemStack[] items;

    // Client interpolation and pos/rot
    @Environment(EnvType.CLIENT)
    private int clientInterpolationSteps;
    @Environment(EnvType.CLIENT)
    private double clientX;
    @Environment(EnvType.CLIENT)
    private double clientY;
    @Environment(EnvType.CLIENT)
    private double clientZ;
    @Environment(EnvType.CLIENT)
    private double clientYaw;
    @Environment(EnvType.CLIENT)
    private double clientPitch;

    // Client velocity
    @Environment(EnvType.CLIENT)
    private double clientVelocityX;
    @Environment(EnvType.CLIENT)
    private double clientVelocityY;
    @Environment(EnvType.CLIENT)
    private double clientVelocityZ;


    /**
     * Primary constructor for the Airship entity
     *
     * @param world The world
     */
    public AirshipEntity(World world) {
        super(world);

        clientInterpolationSteps = 0;
        clientX = clientY = clientZ = clientYaw = 0;
        clientVelocityX = clientVelocityY = clientVelocityZ = 0;

        arrowCounter = 0;
        hasFired = false;
        blocksSameBlockSpawning = true;
        setBoundingBoxSpacing(1.5F, 1.7F);
        standingEyeHeight = height / 2F;
        items = new ItemStack[36];
        renderDistanceMultiplier = 1.5D;
        height = 5.7F;
        width = 1.75F;
    }

    /**
     * Spawning constructor for the Airship entity.
     *
     * @param world The world
     * @param x     X Position
     * @param y     Y Position
     * @param z     Z Position
     */
    public AirshipEntity(World world, double x, double y, double z) {
        this(world);
        setPosition(x, y + standingEyeHeight, z);

        velocityX = 0D;
        velocityY = 0D;
        velocityZ = 0D;

        prevX = x;
        prevY = y;
        prevZ = z;
    }

    @Override
    public boolean shouldRender(double distance) {
        return distance < 10000D;
    }

    @Override
    protected void initDataTracker() {
        dataTracker.startTracking(DATA_BALLOON_COLOR, (byte) 0);
        dataTracker.startTracking(DATA_HURT, (byte) 0);
        dataTracker.startTracking(DATA_HURT_DIR, (byte) 1);
        dataTracker.startTracking(DATA_DAMAGE, (byte) 0);
    }

    @Override
    protected void readNbt(@NotNull NbtCompound nbt) {
        setBalloonColor(nbt.getInt("balloonColor"));
        litTime = nbt.getInt("litTime");
        litDuration = litTime;

        NbtList itemsList = nbt.getList("Items");
        Arrays.fill(items, null);

        for (int i = 0; i < itemsList.size(); ++i) {
            NbtCompound itemAt = (NbtCompound) itemsList.get(i);
            int j = itemAt.getByte("Slot");
            if (j < items.length) {
                items[j] = new ItemStack(itemAt);
            }
        }
    }

    @Override
    public void setVelocityClient(double x, double y, double z) {
        clientVelocityX = velocityX = x;
        clientVelocityY = velocityY = y;
        clientVelocityZ = velocityZ = z;
    }

    @Override
    protected void writeNbt(NbtCompound nbt) {
        NbtList list = new NbtList();

        for (int i = 0; i < items.length; i++) {
            if (items[i] == null) continue;

            NbtCompound compound = new NbtCompound();
            compound.putByte("Slot", (byte) i);
            items[i].writeNbt(compound);
            list.add(compound);
        }

        nbt.put("Items", list);

        nbt.putInt("balloonColor", getBalloonColor());
        nbt.putInt("litTime", litTime);
    }

    public int getBalloonColor() {
        return dataTracker.getByte(DATA_BALLOON_COLOR) & 15;
    }

    public void setBalloonColor(int color) {
        byte colorByte = dataTracker.getByte(DATA_BALLOON_COLOR);
        dataTracker.set(DATA_BALLOON_COLOR, (byte) (colorByte & 240 | color & 15));
    }

    public int getDamage() {
        return dataTracker.getByte(DATA_DAMAGE);
    }

    public void setDamage(int damage) {
        dataTracker.set(DATA_DAMAGE, (byte) damage);
    }

    public int getHurtTime() {
        return dataTracker.getByte(DATA_HURT);
    }

    public void setHurtTime(int hurtTime) {
        dataTracker.set(DATA_HURT, (byte) hurtTime);
    }

    public int getHurtDir() {
        return dataTracker.getByte(DATA_HURT_DIR);
    }

    public void setHurtDir(int hurtDir) {
        dataTracker.set(DATA_HURT_DIR, (byte) hurtDir);
    }

    @Override
    public int size() {
        return 14;
    }

    @Override
    public ItemStack getStack(int slot) {
        return items[slot];
    }

    @Override
    public ItemStack removeStack(int slot, int amount) {
        if (items[slot] == null)
            return null;

        if (items[slot].count <= amount) {
            ItemStack item = items[slot];
            items[slot] = null;
            return item;
        }

        ItemStack sItem = items[slot].split(amount);
        if (items[slot].count == 0)
            items[slot] = null;

        return sItem;
    }

    @Override
    public void setStack(int slot, ItemStack itemInstance) {
        items[slot] = itemInstance;
        if (itemInstance != null && itemInstance.count > getMaxCountPerStack())
            itemInstance.count = getMaxCountPerStack();
    }

    @Override
    public String getName() {
        return "Airship";
    }

    @Override
    public int getMaxCountPerStack() {
        return 64;
    }

    @Override
    public void markDirty() {

    }

    @Override
    public boolean canPlayerUse(PlayerEntity player) {
        return !dead && player.getSquaredDistance(this) <= 64D;
    }

    @Override
    public Identifier getHandlerIdentifier() {
        return Identifier.of(EntityListener.NAMESPACE, "Airship");
    }

    @Environment(EnvType.CLIENT)
    @Override
    public float getShadowRadius() {
        return 0F;
    }

    @Environment(EnvType.CLIENT)
    public int getLitProgress(int i) {
        if (litDuration == 0) litDuration = 200;
        return this.litTime * i / litDuration;
    }

    private int getLitTime(ItemStack itemStack) {
        if (itemStack == null) return 0;

        Item item = itemStack.getItem();
        if (item.id == Item.GUNPOWDER.id) return 200;

        return FuelRegistry.getFuelTime(itemStack);
    }

    @Override
    public void markDead() {
        for (int i = 0; i < size(); i++) {
            ItemStack is = getStack(i);
            if (is != null) {
                float rx = random.nextFloat() * 0.8F + 0.1F;
                float ry = random.nextFloat() * 0.8F + 0.1F;
                float rz = random.nextFloat() * 0.8F + 0.1F;

                while (is.count > 0) {
                    int randSplit = random.nextInt(21) + 10;
                    if (randSplit > is.count) randSplit = is.count;

                    is.count -= randSplit;
                    ItemEntity item = new ItemEntity(world,
                            x + rx,
                            y + ry,
                            z + rz,
                            new ItemStack(is.itemId, randSplit, is.getDamage()));

                    float m = 0.05F;
                    item.velocityX = random.nextGaussian() * m;
                    item.velocityY = random.nextGaussian() * m + 0.2F;
                    item.velocityZ = random.nextGaussian() * m;

                    world.spawnEntity(item);
                }
            }
        }

        for (int i = 1; i < 30; ++i) {
            if (i % 2 == 0) {
                world.addParticle("AirshipsSteam",
                        x + (double) random.nextInt(i) / 8,
                        y,
                        z + (double) random.nextInt(i) / 8,
                        0D,
                        0D,
                        0D);

                world.addParticle("AirshipsSteam",
                        x + (double) random.nextInt(i) / 8,
                        y,
                        z - (double) random.nextInt(i) / 8,
                        0D,
                        0D,
                        0D);
            } else {
                world.addParticle("AirshipsSteam",
                        x - (double) random.nextInt(i) / 8,
                        y,
                        z + (double) random.nextInt(i) / 8,
                        0D,
                        0D,
                        0D);

                world.addParticle("AirshipsSteam",
                        x - (double) random.nextInt(i) / 8,
                        y,
                        z - (double) random.nextInt(i) / 8,
                        0D,
                        0D,
                        0D);
            }
        }

        super.markDead();
    }

    /**
     * A method to damage the Airship.
     * <p>In this case it spawns steam between the damage source and ship.</p>
     * <p>It also handles 'hurt dir' and time, like the boat.</p>
     *
     * @param source     The entity that caused the damage.
     * @param hurtDamage The amount of damage dealt.
     * @return always true
     */
    @Override
    public boolean damage(Entity source, int hurtDamage) {
        if (source == null) return true;
        if (world.isRemote || dead) return true;

        double ex = source.x - x;
        double ey = source.y - y;
        double ez = source.z - z;

        double mx = source.x - ex / 2D;
        double my = source.y - ey / 2D;
        double mz = source.z - ez / 2D;
        world.addParticle("AirshipsSteam", mx, my, mz, 0D, 0D, 0D);

        setHurtDir(-getHurtDir());
        setHurtTime(10);// Shark - This was originally set to 1, or "no bounce"
        setDamage(getDamage() + hurtDamage * 10);

        scheduleVelocityUpdate();

        if (getDamage() > 20 * 4) {
            dropItem(ItemListener.ITEM_AIRSHIP.id, 1, 0F);
            markDead();
        }

        return true;
    }

    /**
     * A method called when interacting (right-clicking) the airship.
     * <p>This handles riding and dying the balloon.</p>
     *
     * @param player The player
     * @return always true
     */
    @Override
    public boolean interact(PlayerEntity player) {
        if (passenger != null && passenger instanceof PlayerEntity && player != passenger) return true;
        if (world.isRemote) return true;

        if (player.isSneaking()) {
            if (player.inventory.getSelectedItem() == null) return true;
            ItemStack heldItem = player.inventory.getSelectedItem();

            if (heldItem.getItem() instanceof DyeItem) {
                int dyeDamage = heldItem.getDamage();
                if (getBalloonColor() == 15 - dyeDamage)
                    return true;

                int dyeIndex = 15 - dyeDamage;
                setBalloonColor(dyeIndex);

                heldItem.count--;
                if (heldItem.count <= 0) player.inventory.setStack(player.inventory.selectedSlot, null);
            }

            return true;
        }

        player.setVehicle(this);

        return true;
    }

    @Override
    public double getPassengerRidingHeight() {
        return height * 0F - 0.3F;
    }

    @Override
    public void updatePassengerPosition() {
        if (passenger == null) return;

        double xa = Math.cos(yaw * Math.PI / 180) * 0.4;
        double za = Math.sin(yaw * Math.PI / 180) * 0.4;
        passenger.setPosition(x + xa, y + getPassengerRidingHeight() + passenger.getStandingEyeHeight(), z + za);
    }

    @Override
    public boolean isCollidable() {
        return !dead;
    }

    @Environment(EnvType.CLIENT)
    @Override
    public void setPositionAndAnglesAvoidEntities(double x, double y, double z, float pitch, float yaw, int interpolationSteps) {
        clientX = x;
        clientY = y;
        clientZ = z;

        clientPitch = pitch;
        clientYaw = yaw;
        clientInterpolationSteps = interpolationSteps + 4;

        velocityX = clientVelocityX;
        velocityY = clientVelocityY;
        velocityZ = clientVelocityZ;
    }

    @Override
    public void tick() {
        super.tick();

        if (getHurtTime() > 0) setHurtTime(getHurtTime() - 1);
        if (getDamage() > 0) setDamage(getDamage() - 1);

        prevX = x;
        prevY = y;
        prevZ = z;

        int steps = 5;
        double waterPercent = 0D;

        for (int i = 0; i < steps; ++i) {
            double y0 = boundingBox.minY + 0.75 + (boundingBox.maxY - boundingBox.minY + 0.75) * (i) / steps - 2 / 16F;
            double y1 = boundingBox.minY + 0.75 + (boundingBox.maxY - boundingBox.minY + 0.75) * (i + 1) / steps - 2 / 16F;
            Box bb2 = Box.createCached(boundingBox.minX, y0, boundingBox.minZ, boundingBox.maxX, y1, boundingBox.maxZ);
            if (world.isFluidInBox(bb2, Material.WATER)) waterPercent += 1D / steps;
        }

        double lastSpeed = Math.sqrt(velocityX * velocityX + velocityZ * velocityZ);
        if (world.isFluidInBox(boundingBox, Material.WATER)) {
            if (lastSpeed > 0.15) {
                double xa = Math.cos(yaw * Math.PI / 180D);
                double za = Math.sin(yaw * Math.PI / 180D);

                for (int i = 0; (double) i < (double) 1.0F + lastSpeed * (double) 60.0F; ++i) {
                    double side1 = (random.nextFloat() * 2F - 1F);
                    double side2 = (random.nextInt(2) * 2 - 1) * 0.7D;
                    if (random.nextBoolean()) {
                        double xx = x - xa * side1 * 0.8 + za * side2;
                        double zz = z - za * side1 * 0.8 - xa * side2;
                        world.addParticle("splash", xx, y - 2 / 16F, zz, velocityX, velocityY, velocityZ);
                    } else {
                        double xx = x + xa + za * side1 * 0.7;
                        double zz = z + za - xa * side1 * 0.7;
                        world.addParticle("splash", xx, y - 2 / 16F, zz, velocityX, velocityY, velocityZ);
                    }
                }
            }
        }

        // Show steam particles when/if possible.
        if (Airships.MISC_CONFIG.showBoiler) {
            double side = random.nextFloat() * 2F - 1F;
            if (side > 0.65D)
                world.addParticle("AirshipsSteam", x, y + 0.9D - side, z, 0D, 0D, 0D);
        }

        if (world.isRemote && passenger != null) {
            if (clientInterpolationSteps > 0) {
                double xt = x + (clientX - x) / clientInterpolationSteps;
                double yt = y + (clientY - y) / clientInterpolationSteps;
                double zt = z + (clientZ - z) / clientInterpolationSteps;

                double yrd = clientPitch - yaw;

                while (yrd < 180F) yrd += 360F;
                while (yrd > 180.0F) yrd -= 360.0F;

                yaw += (float) (yrd / clientInterpolationSteps);
                pitch += (float) ((clientYaw - pitch) / clientInterpolationSteps);

                clientInterpolationSteps--;

                setPosition(xt, yt, zt);
                setRotation(yaw, pitch);
            } else {
                move(velocityX, velocityY, velocityZ);

                if (onGround) {
                    velocityX *= 0.5F;
                    velocityY *= 0.5F;
                    velocityZ *= 0.5F;
                }

                velocityX *= 0.99F;
                velocityY *= 0.95F;
                velocityZ *= 0.99F;

                double xt = x;
                double yt = y + velocityY;
                double zt = z;
                setPosition(xt, yt, zt);

                velocityX *= 0.5F;
                velocityY *= 0.5F;
                velocityZ *= 0.5F;
            }

            return;
        }

        if (waterPercent > 0.1D) {
            double bob = waterPercent * 2F - 1F;
            velocityY -= (litTime > 0 ? 0.02F : 0.01F) * bob;
        }

        // Start to descend if the passenger leaves/dies, or if the fuel runs out.
        if (((passenger == null || passenger.dead) || litTime <= 0) && waterPercent <= 0D)
            velocityY = Math.max(-0.04D, velocityY - 0.01D);

        if (passenger != null && passenger instanceof LivingEntity) {
            velocityX += passenger.velocityX * 0.2;
            velocityZ += passenger.velocityZ * 0.2;

            if (clientPressingUp) velocityY -= passenger.velocityY * 0.04D;
            if (clientPressingDown) velocityY += passenger.velocityY * 0.04D;
        }
        
        if (velocityX < -0.4D) velocityX = -0.4D;
        if (velocityX > 0.4D) velocityX = 0.4D;

        if (velocityZ < -0.4D) velocityZ = -0.4D;
        if (velocityZ > 0.4D) velocityZ = 0.4D;

        if (onGround) {
            velocityX *= 0.5F;
            velocityY *= 0.5F;
            velocityZ *= 0.5F;
        }

        move(velocityX, velocityY, velocityZ);

        if (horizontalCollision && lastSpeed > 0.20) {
            if (!world.isRemote && !dead) 
                setDamage(getDamage() + 10);
        } else {
            velocityX *= 0.99F;
            velocityY *= 0.95F;
            velocityZ *= 0.99F;
        }

        pitch = 0F;
        double yawT = yaw;
        double xDiff = prevX - x;
        double zDiff = prevZ - z;
        if (xDiff * xDiff + zDiff * zDiff > 0.001) yawT = (float) (Math.atan2(zDiff, xDiff) * (double) 180F / Math.PI);

        double rotDiff = yawT - yaw;
        while (rotDiff > 180F) rotDiff -= 360F;
        while (rotDiff < -180F) rotDiff += 360F;

        if (rotDiff > 20F) rotDiff = 20F;
        if (rotDiff < -20F) rotDiff = -20F;

        yaw += (float) rotDiff;
        this.setRotation(this.yaw, this.pitch);

        // Server-side stuff past this point!
        // This handles collision, setting the passenger is null, firing arrows, and updating the fuel.
        if (world.isRemote) return;

        List<Entity> var16 = world.getEntities(this, boundingBox.expand(0.2F, 0.0F, 0.2F));
        if (var16 != null && !var16.isEmpty()) {
            for (Entity entity : var16) {
                if (entity != passenger && entity.isPushable() && entity instanceof AirshipEntity) {
                    entity.onCollision(this);
                }
            }
        }

        if (this.passenger != null && this.passenger.dead) {
            this.passenger = null;
        }


        // Fuel lighting
        if (litTime > 0)
            litTime--;

        if (litTime <= 0) {
            boost = false;
            litDuration = litTime = getLitTime(items[13]);

            if (litTime > 0 && items[13] != null) {
                if (items[13].getItem() == Item.GUNPOWDER)
                    boost = true;

                if (items[13].getItem().hasCraftingReturnItem())
                    items[13] = new ItemStack(items[13].getItem().getCraftingReturnItem());
                else
                    items[13].count--;
            }
        }

        if (items[13] != null && items[13].count <= 0) items[13] = null;


        // Arrow firing //
        hasFired = arrowCounter != 20;
        if (arrowCounter < 20)
            arrowCounter++;

        if (clientPressingFire && passenger != null && !hasFired) {
            PlayerEntity player = (PlayerEntity) passenger;
            fireProjectile(player);
            arrowCounter = 0;
        }
    }

    private void fireProjectile(PlayerEntity player) {
        ItemStack itemStack = getStack(12);
        if (itemStack == null) return;

        Vec3d playerLook = player.getLookVector();
        double lx = x + (playerLook.x * 4D);
        double ly = y + (height / 8);
        double lz = z + (playerLook.z * 4D);

        if (itemStack.getItem() == Item.ARROW) {
            ArrowEntity arrow = new ArrowEntity(world, player);
            arrow.setPosition(lx, ly, lz);
            arrow.setVelocity(playerLook.x, playerLook.y, playerLook.z, 2.6F, 6F);
            removeStack(12, 1);
            world.playSound(player, "random.bow", 1F, 1F / (random.nextFloat() * 0.4F + 0.8F));
            world.spawnEntity(arrow);
            return;
        }

        if (itemStack.getItem() == Item.SNOWBALL) {
            SnowballEntity snowball = new SnowballEntity(world, player);
            snowball.setPosition(lx, ly, lz);
            snowball.setVelocity(playerLook.x, playerLook.y, playerLook.z, 2.6F, 6F);
            removeStack(12, 1);
            world.playSound(player, "random.bow", 1F, 1F / (random.nextFloat() * 0.4F + 0.8F));
            world.spawnEntity(snowball);
            return;
        }

        if (itemStack.getItem() == Item.EGG) {
            EggEntity egg = new EggEntity(world, player);
            egg.setPosition(lx, ly, lz);
            egg.setVelocity(playerLook.x, playerLook.y, playerLook.z, 2.6F, 6F);
            removeStack(12, 1);
            world.playSound(player, "random.bow", 1F, 1F / (random.nextFloat() * 0.4F + 0.8F));
            world.spawnEntity(egg);
            return;
        }

        Runnable consumeArrow = () -> {
            removeStack(12, 1);
            world.playSound(player, "random.bow", 1F, 1F / (random.nextFloat() * 0.4F + 0.8F));
        };

        if (ELEMENTAL_ARROWS) {
            try {
                ArrowsIntegration.tryFireArrow(world, player, getStack(12), lx, ly, lz, playerLook, consumeArrow);
            } catch (Exception ignored) {
            }
        }

        if (WOLVES) {
            try {
                WolvesIntegration.tryFireArrow(world, player, getStack(12), lx, ly, lz, playerLook, consumeArrow);
            } catch (Exception ignored) {
            }
        }
    }

    @Override
    protected void fall(double heightDifference, boolean onGround) {
    }

    @Override
    protected void onLanding(float fallDistance) {
    }

    @Override
    public boolean isPushable() {
        return !dead;
    }
}