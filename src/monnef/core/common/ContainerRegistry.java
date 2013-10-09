/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.core.common;

import monnef.core.block.ContainerMonnefCore;
import monnef.core.client.GuiContainerMonnefCore;
import monnef.core.utils.ReflectionTools;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.tileentity.TileEntity;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Constructor;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class ContainerRegistry {
    public static final String CANNOT_INSTANTIATE_CONTAINER = "Cannot instantiate container.";
    private static HashMap<Class<? extends TileEntity>, MachineItem> db = new HashMap<Class<? extends TileEntity>, MachineItem>();

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.TYPE)
    public @interface ContainerTag {
        int slotsCount();

        int outputSlotsCount() default 1;

        int inputSlotsCount() default -1;
    }

    public static class ContainerDescriptor {
        private final int slotsCount;
        private final int outputSlotsCount;
        private final int inputSlotsCount;

        public ContainerDescriptor(ContainerTag tag) {
            slotsCount = tag.slotsCount();
            outputSlotsCount = tag.outputSlotsCount();
            inputSlotsCount = tag.inputSlotsCount() == -1 ? slotsCount - outputSlotsCount : tag.inputSlotsCount();
        }

        public int getSlotsCount() {
            return slotsCount;
        }

        public int getOutputSlotsCount() {
            return outputSlotsCount;
        }

        public int getInputSlotsCount() {
            return inputSlotsCount;
        }

        public int getStartIndexOfOutput() {
            return getSlotsCount() - getOutputSlotsCount();
        }
    }

    public static class MachineItem {
        public final Class<? extends TileEntity> tileClass;
        public final Class<? extends ContainerMonnefCore> containerClass;
        private Class<?> guiClass; // GuiContainerBasicProcessingMachine

        public final ContainerDescriptor containerPrototype;
        private Constructor<?> guiConstructor; // GuiContainerBasicProcessingMachine
        public final Constructor<? extends ContainerMonnefCore> containerConstructor;

        private MachineItem(Class<? extends TileEntity> tileClass, Class<? extends ContainerMonnefCore> containerClass, ContainerDescriptor containerPrototype) {
            this.tileClass = tileClass;
            this.containerClass = containerClass;
            this.containerPrototype = containerPrototype;

            if (containerClass.getConstructors().length != 1) {
                throw new RuntimeException("Multiple constructors of container class.");
            }
            Constructor<?> constructor = containerClass.getConstructors()[0];
            if (constructor.getParameterTypes().length != 2) {
                throw new RuntimeException("Wrong count of parameters of container constructor.");
            }
            if (!InventoryPlayer.class.isAssignableFrom(constructor.getParameterTypes()[0]) ||
                    !TileEntity.class.isAssignableFrom(constructor.getParameterTypes()[1])) {
                throw new RuntimeException("Incorrect type of parameters of container constructor.");
            }
            //this.containerConstructor = containerClass.getConstructor(InventoryPlayer.class, TileEntity.class);
            this.containerConstructor = (Constructor<? extends ContainerMonnefCore>) constructor;
        }

        private void setGuiConstructor(Constructor<?> guiConstructor) {
            if (this.guiConstructor != null) throw new RuntimeException("GUI constructor already set");
            this.guiConstructor = guiConstructor;
        }

        public void setGuiClass(Class<?> guiClass) {
            this.guiClass = guiClass;
            if (guiClass != null) {
                if (guiClass.getConstructors().length != 1) {
                    throw new RuntimeException("Multiple constructors of GUI class.");
                }
                Constructor<?> constructor = guiClass.getConstructors()[0];
                if (constructor.getParameterTypes().length != 3) {
                    throw new RuntimeException("Wrong count of parameters of GUI constructor.");
                }
                if (!InventoryPlayer.class.isAssignableFrom(constructor.getParameterTypes()[0]) ||
                        !TileEntity.class.isAssignableFrom(constructor.getParameterTypes()[1]) ||
                        !ContainerMonnefCore.class.isAssignableFrom(constructor.getParameterTypes()[2])) {
                    throw new RuntimeException("Incorrect type of parameters of GUI constructor.");
                }
                setGuiConstructor(constructor);
            } else setGuiConstructor(null);
        }

        public Constructor<?> getGuiConstructor() {
            return guiConstructor;
        }

        public Class<?> getGuiClass() {
            return guiClass;
        }
    }

    public static void register(Class<? extends TileEntity> clazz, Class<? extends ContainerMonnefCore> container) {
        if (db.containsKey(clazz)) {
            throw new RuntimeException("containerPrototype already contains this class, cannot re-register");
        }
        ContainerTag tag = ReflectionTools.findAnnotation(ContainerTag.class, clazz);
        if (tag == null) {
            throw new RuntimeException(ContainerTag.class.getSimpleName() + " not found on " + clazz.getSimpleName());
        }

        db.put(clazz, new MachineItem(clazz, container, new ContainerDescriptor(tag)));
    }

    public static void registerOnClient(Class<? extends TileEntity> clazz, Class<?> gui) {
        MachineItem item = db.get(clazz);
        if (item == null) throw new RuntimeException("Registering GUI container with unknown TE.");
        if (!GuiContainerMonnefCore.class.isAssignableFrom(gui)) {
            throw new RuntimeException("Class doesn't inherit from proper ancestor!");
        }
        item.setGuiClass(gui);
    }

    public static void assertAllItemsHasGuiClass() {
        for (Map.Entry<Class<? extends TileEntity>, MachineItem> item : db.entrySet()) {
            if (item.getValue().getGuiClass() == null) {
                throw new RuntimeException("TE " + item.getKey().getSimpleName() + " is missing GUI mapping.");
            }
        }
    }

    public static ContainerDescriptor getContainerPrototype(Class<? extends TileEntity> clazz) {
        if (!db.containsKey(clazz)) {
            throw new RuntimeException("Query for not registered TE named " + clazz.getSimpleName());
        }
        return db.get(clazz).containerPrototype;
    }

    public static Collection<Class<? extends TileEntity>> getTileClasses() {
        return db.keySet();
    }

    public static ContainerMonnefCore createContainer(TileEntity tile, InventoryPlayer inventory) {
        try {
            return db.get(tile.getClass()).containerConstructor.newInstance(inventory, tile);
        } catch (Throwable e) {
            throw new RuntimeException("Cannot create new container for tile class: " + tile.getClass().getSimpleName());
        }
    }

    public static MachineItem getItem(Class tileClass) {
        return db.get(tileClass);
    }

    public static boolean containsRegistration(TileEntity tile) {
        return db.containsKey(tile.getClass());
    }

    public static Object createGui(TileEntity tile, InventoryPlayer inventory) {
        try {
            return ContainerRegistry.getItem(tile.getClass()).getGuiConstructor().newInstance(inventory, tile, ContainerRegistry.createContainer(tile, inventory));
        } catch (Throwable e) {
            throw new RuntimeException("Cannot create new GUI for container for tile class: " + tile.getClass().getSimpleName());
        }
    }
}
