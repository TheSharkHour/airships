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
    private double clientPitch;
    @Environment(EnvType.CLIENT)
    private double clientYaw;

    // Client velocity
    @Environment(EnvType.CLIENT)
    private double clientVelocityX;
    @Environment(EnvType.CLIENT)
    private double clientVelocityY;
    @Environment(EnvType.CLIENT)
    private double clientVelocityZ;

    // Boat-style damage rotation
    public int damage;
    public int hurtTime;
    public int hurtDir;


    /**
     * Primary constructor for the Airship entity
     * @param world The world
     */
    public AirshipEntity(World world) {
        super(world);
        this.arrowCounter = 0;
        this.hasFired = false;
        this.damage = 0;
        this.hurtTime = 0;
        this.hurtDir = 1;
        this.blocksSameBlockSpawning = true;
        this.setBoundingBoxSpacing(1.5F, 1.7F);
        this.standingEyeHeight = this.height / 2.0F;
        this.items = new ItemStack[36];
        this.renderDistanceMultiplier = 1.5D;
    }

    /**
     * Spawning constructor for the Airship entity.
     * @param world The world
     * @param x X Position
     * @param y Y Position
     * @param z Z Position
     */
    public AirshipEntity(World world, double x, double y, double z) {
        this(world);
        this.setPosition(x, y + standingEyeHeight, z);
        this.velocityX = 0D;
        this.velocityY = 0D;
        this.velocityZ = 0D;
        this.prevX = x;
        this.prevY = y;
        this.prevZ = z;
    }

    @Override
    public boolean shouldRender(double distance) {
        return distance < 10000D;
    }

    @Override
    protected void initDataTracker() {
        dataTracker.startTracking(DATA_BALLOON_COLOR, (byte) 0);
    }

    @Override
    protected void readNbt(@NotNull NbtCompound nbt) {
        setBalloonColor(nbt.getInt("balloonColor"));
        litTime = nbt.getInt("litTime");
        litDuration = litTime;

        NbtList itemsList = nbt.getList("Items");
        Arrays.fill(items, null);

        for(int i = 0; i < itemsList.size(); ++i) {
            NbtCompound itemAt = (NbtCompound)itemsList.get(i);
            int j = itemAt.getByte("Slot");
            if(j < items.length) {
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
        if (this.items[slot] == null) return null;
        if (this.items[slot].count <= amount) {
            ItemStack item = this.items[slot];
            this.items[slot] = null;
            return item;
        }

        ItemStack sItem = this.items[slot].split(amount);
        if (this.items[slot].count == 0) this.items[slot] = null;

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

        for(int i = 1; i < 30; ++i) {
            if(i % 2 == 0) {
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
     * @param damageSource The entity that caused the damage.
     * @param amount The amount of damage dealt.
     * @return always true
     */
    @Override
    public boolean damage(Entity damageSource, int amount) {
        if (damageSource == null) return true;
        if (world.isRemote || dead) return true;

        double ex = damageSource.x - x;
        double ey = damageSource.y - y;
        double ez = damageSource.z - z;

        double mx = damageSource.x - ex / 2D;
        double my = damageSource.y - ey / 2D;
        double mz = damageSource.z - ez / 2D;
        world.addParticle("AirshipsSteam", mx, my, mz, 0D, 0D, 0D);

        hurtDir = -hurtDir;
        hurtTime = 10; // This was originally set to 1, or "no bounce"
        damage += amount * 10;
        this.scheduleVelocityUpdate();

        if (damage > 300) {
            dropItem(ItemListener.ITEM_AIRSHIP.id, 1, 0F);
            markDead();
        }

        return true;
    }

    /**
     * A method called when interacting (right-clicking) the airship.
     * <p>This handles riding and dying the balloon.</p>
     * @param player The player
     * @return always true
     */
    @Override
    public boolean interact(PlayerEntity player) {
        if (passenger != null && passenger instanceof PlayerEntity && player != passenger) return true;
        if (!world.isRemote) {
            if (player.isSneaking()) {
                if (player.inventory.getSelectedItem() == null) return true;
                ItemStack heldItem = player.inventory.getSelectedItem();

                if (heldItem.getItem() instanceof DyeItem) {
                    int dyeDamage = heldItem.getDamage();
                    if (getBalloonColor() == 15 - dyeDamage) return true;

                    int dyeIndex = 15 - dyeDamage;
                    setBalloonColor(dyeIndex);

                    heldItem.count--;
                    if (heldItem.count <= 0) {
                        player.inventory.setStack(player.inventory.selectedSlot, null);
                    }
                }

                return true;
            }

            player.setVehicle(this);
        }

        return true;
    }

    @Override
    public double getPassengerRidingHeight() {
        return  this.height * 0D - 0.3D;
    }

    @Override
    public boolean isCollidable() {
        return !dead;
    }

    @Environment(EnvType.CLIENT)
    @Override
    public void setPositionAndAnglesAvoidEntities(double x, double y, double z, float pitch, float yaw, int interpolationSteps) {
        this.clientX = x;
        this.clientY = y;
        this.clientZ = z;

        this.clientPitch = pitch;
        this.clientYaw = yaw;
        this.clientInterpolationSteps = interpolationSteps + 4;

        this.velocityX = clientVelocityX;
        this.velocityY = clientVelocityY;
        this.velocityZ = clientVelocityZ;
    }

    @Override
    public void tick() {
        super.tick();
        updateFuel();

        if (boost) {
            this.velocityX += this.passenger.velocityX * 18.0D;
            this.velocityZ += this.passenger.velocityZ * 18.0D;
        }

        if (world.isRemote) {
            tickClient();
        } else {
            tickServer();
        }

        updatePhysics();
        updateRotation();
        handleCollisions();
        handlePassenger();

        if (hurtTime > 0) hurtTime--;
        if (damage > 0) damage--;
    }

    /**
     * A method to handle updating the fuel.
     * <p>If the fuel is below (or equal to) zero, it grabs new fuel.</p>
     * <p>Important to note that Gunpowder can be used for 200 ticks and will BOOST the airship.</p>
     */
    private void updateFuel() {
        if (world.isRemote) return;

        if (litTime > 0) litTime--;
        if (litTime <= 0) {
            boost = false;
            litDuration = litTime = getLitTime(items[13]);

            if (litTime > 0 && items[13] != null) {
                if (items[13].getItem() == Item.GUNPOWDER) {
                    boost = true;
                }

                if (items[13].getItem().hasCraftingReturnItem()) {
                    items[13] = new ItemStack(items[13].getItem().getCraftingReturnItem());
                } else {
                    items[13].count--;
                }
            }
        }

        if (items[13] != null && items[13].count <= 0) items[13] = null;
    }

    @Environment(EnvType.CLIENT)
    private void tickClient() {
        if (clientInterpolationSteps > 0) interpolatePosition();
        else applyVelocity();
    }

    /**
     * A client-side method to interpolate the position and rotation of the airship.
     */
    @Environment(EnvType.CLIENT)
    private void interpolatePosition() {
        if (world.isRemote) {
            double cx = this.x + (this.clientX - this.x) / (double)this.clientInterpolationSteps;
            double cy = this.y + (this.clientY - this.y) / (double)this.clientInterpolationSteps;
            double cz = this.z + (this.clientZ - this.z) / (double)this.clientInterpolationSteps;

            double cYaw = clientPitch - yaw;
            while (cYaw < -180D) cYaw += 360.0D;
            while (cYaw > 180D) cYaw -= 360.0D;

            yaw = (float) (yaw + cYaw / clientInterpolationSteps);
            pitch = (float) (pitch + (clientYaw - pitch) / clientInterpolationSteps);
            --clientInterpolationSteps;

            setPosition(cx, cy, cz);
            setRotation(yaw, pitch);
        }
    }

    /**
     * A client-side method to apply the velocity of the airship.
     * <p>Speed is halved when on the ground.</p>
     */
    @Environment(EnvType.CLIENT)
    private void applyVelocity() {
        double cx = x + velocityX;
        double cy = y + velocityY;
        double cz = z + velocityZ;
        setPosition(cx, cy, cz);

        if (onGround) {
            velocityX *= 0.5D;
            velocityY *= 0.5D;
            velocityZ *= 0.5D;
            y += 3D;
        }

        velocityX *= 0.99D;
        velocityY *= 0.95D;
        velocityZ *= 0.99D;
    }

    private void tickServer() {
        handlePlayerControls();
        applyGravity();
        clampVelocity();
        applyGroundFriction();

        move(velocityX, velocityY, velocityZ);
        setRotation(yaw, pitch);

        spawnWaterParticles();
        spawnSteamParticles();
    }

    /**
     * A method to handle player controls.
     * <p>This can cause weirdness when using a mod that adds shift to exit.</p>
     */
    private void handlePlayerControls() {
        if (passenger != null) {
            velocityX += passenger.velocityX * 0.25D;
            velocityZ += passenger.velocityZ * 0.25D;

            if (clientPressingUp) {
                velocityY -= passenger.velocityY * 0.04D;
            }

            if (clientPressingDown) {
                handleDescend();
            }
        }
    }

    /**
     * A method to handle descending in the airship.
     */
    private void handleDescend() {
        for (int i = 0; i < 5; i++) {
            double d4 = boundingBox.minY + (boundingBox.maxY - boundingBox.minY) * (double)(i - 2) / (double)5 - 0.125D;
            double d8 = boundingBox.minY + (boundingBox.maxY - boundingBox.minY) * (double)(i - 4) / (double)5 - 0.125D;
            Box aabb = Box.createCached(this.boundingBox.minX, d4, this.boundingBox.minZ, this.boundingBox.maxX, d8, this.boundingBox.maxZ);
            if (!world.isMaterialInBox(aabb, Material.WATER)) {
                velocityY += passenger.velocityY * 0.01D;
            } else {
                x += 5D;
                velocityY = 0D;
            }
        }
    }

    /**
     * A method to apply gravity to the airship, with NO passenger.
     */
    private void applyGravity() {
        if (passenger == null || litTime <= 0) {
            velocityY -= 0.06D;
        }
    }

    /**
     * A method to clamp the velocity of the airship.
     */
    private void clampVelocity() {
        if (velocityX < -1D) velocityX = -1D;
        if (velocityX > 1D) velocityX = 1D;
        if (velocityZ < -1D) velocityZ = -1D;
        if (velocityZ > 1D) velocityZ = 1D;
    }

    /**
     * A method to apply ground friction to the airship.
     * <p>Basically it halves the movement speed.</p>
     */
    private void applyGroundFriction() {
        if (onGround) {
            velocityX *= 0.5D;
            velocityY *= 0.5D;
            velocityZ *= 0.5D;
        }
    }

    private void spawnWaterParticles() {
        double sqrt = Math.sqrt(velocityX * velocityX + velocityZ * velocityZ);
        if (sqrt <= 0.15D) return;

        double mc = Math.cos(yaw / 180.0F * Math.PI);
        double ms = Math.sin(yaw / 180.0F * Math.PI);

        for (int i = 0; i < 1D + sqrt * 60; i++) {
            double d18 = random.nextFloat() * 2.0F - 1.0F;
            double d20 = (double)(random.nextInt(2) * 2 - 1) * 0.7D;

            double minY = this.boundingBox.minY + (this.boundingBox.maxY - this.boundingBox.minY) * (double)(i) / 5.0D - 0.125D;
            double maxY = this.boundingBox.minY + (this.boundingBox.maxY - this.boundingBox.minY) * (double)(i + 1) / 5.0D - 0.125D;
            Box aabb = Box.createCached(this.boundingBox.minX, minY, this.boundingBox.minZ, this.boundingBox.maxX, maxY, this.boundingBox.maxZ);

            double px;
            double pz;
            if (random.nextBoolean()) {
                px = this.x - mc * d18 * 0.8D + ms * d20;
                pz = this.z - ms * d18 * 0.8D - mc * d20;
            } else {
                px = this.x + mc + ms * d18 * 0.7D;
                pz = this.z + ms + mc * d18 * 0.7D;
            }

            if (world.isMaterialInBox(aabb, Material.WATER))
                world.addParticle("splash", px, y - 0.125D, pz, velocityX, velocityY, velocityZ);
        }
    }

    private void spawnSteamParticles() {
        if (Airships.MISC_CONFIG.showBoiler) {
            double offX = random.nextFloat() * 2.0F - 1.0F;
            if (offX > 0.65D) {
                world.addParticle("AirshipsSteam", x - offX * 0.D, y + 0.9D, z, 0.0D, 0.0D, 0.0D);
            }
        }
    }

    private void updatePhysics() {
        double sqrt = Math.sqrt(velocityX * velocityX + velocityZ * velocityZ);
        if (!horizontalCollision || sqrt <= 0.15D) {
            velocityX *= 0.99D;
            velocityY *= 0.95D;
            velocityZ *= 0.99D;
        }
    }

    private void updateRotation() {
        if (world.isRemote) return;

        pitch = 0F;
        double nYaw = yaw;
        double nx = prevX - x;
        double nz = prevZ - z;
        if (nx * nx + nz * nz > 0.001D) {
            nYaw = (float) (Math.atan2(nz, nx) * 180D / Math.PI);
        }

        double nnYaw = nYaw - yaw;
        while (nnYaw < -180.0D) nnYaw += 360.0D;
        while (nnYaw > 180.0D) nnYaw -= 360.0D;
        this.yaw = (float) (yaw + nnYaw);
        setRotation(yaw, pitch);
    }

    private void handleCollisions() {
        List<Entity> entities = world.getEntities(this, boundingBox.expand(0.2D, 0D, 0.2D));
        if (entities != null && !entities.isEmpty()) {
            for (Entity entity : entities) {
                if (entity != passenger && entity.isPushable() && entity instanceof AirshipEntity)
                    entity.onCollision(this);
            }
        }
    }

    private void handlePassenger() {
        if (passenger != null && passenger.dead) {
            passenger = null;
        }

        hasFired = arrowCounter != 20;
        if (arrowCounter < 20) arrowCounter++;

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
        double ly = y + (height / 4);
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
                Airships.LOGGER.error("Elemental Arrows integration failed to fire an arrow!");
            }
        }

        if (WOLVES) {
            try {
                WolvesIntegration.tryFireArrow(world, player, getStack(12), lx, ly, lz, playerLook, consumeArrow);
            } catch (Exception ignored) {
                Airships.LOGGER.error("BTW integration failed to fire an arrow!");
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