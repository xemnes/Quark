function initializeCoreMod() {
    return {
        'do-move-replace-structure-helper': {
            'target': {
                'type': 'METHOD',
                'class': 'net.minecraft.block.PistonBlock',
                'methodName': 'func_176319_a', // doMove
                'methodDesc': '(Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/util/Direction;Z)Z'
            },
            'transformer': function(method) {
                var ASM = Java.type('net.minecraftforge.coremod.api.ASMAPI');
                var Opcodes = Java.type('org.objectweb.asm.Opcodes');
                var VarInsnNode = Java.type('org.objectweb.asm.tree.VarInsnNode');
                var InsnList = Java.type('org.objectweb.asm.tree.InsnList');

                var target = ASM.findFirstMethodCall(method, 
                    ASM.MethodType.SPECIAL, 
                    "net/minecraft/block/state/PistonBlockStructureHelper",
                    "<init>",
                    "(Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/util/Direction;Z)V");
                
                var newInstructions = new InsnList();
                newInstructions.add(new VarInsnNode(Opcodes.ALOAD, 1));
                newInstructions.add(new VarInsnNode(Opcodes.ALOAD, 2));
                newInstructions.add(new VarInsnNode(Opcodes.ALOAD, 3));
                newInstructions.add(new VarInsnNode(Opcodes.ILOAD, 4));
                newInstructions.add(ASM.buildMethodCall(
                    "vazkii/quark/base/handler/AsmHooks", 
                    "transformStructureHelper",
                    "(Lnet/minecraft/block/state/PistonBlockStructureHelper;Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/util/Direction;Z)Lnet/minecraft/block/state/PistonBlockStructureHelper;", 
                    ASM.MethodType.STATIC
                ));

                method.instructions.insert(target, newInstructions);

                return method;
            }
        },

        'check-for-move-replace-structure-helper': {
            'target': {
                'type': 'METHOD',
                'class': 'net.minecraft.block.PistonBlock',
                'methodName': 'func_176316_e', // checkForMove
                'methodDesc': '(Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;)V'
            },
            'transformer': function(method) {
                var ASM = Java.type('net.minecraftforge.coremod.api.ASMAPI');
                var Opcodes = Java.type('org.objectweb.asm.Opcodes');
                var VarInsnNode = Java.type('org.objectweb.asm.tree.VarInsnNode');
                var InsnNode = Java.type('org.objectweb.asm.tree.InsnNode');
                var InsnList = Java.type('org.objectweb.asm.tree.InsnList');

                var target = ASM.findFirstMethodCall(method, 
                    ASM.MethodType.SPECIAL, 
                    "net/minecraft/block/state/PistonBlockStructureHelper",
                    "<init>",
                    "(Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/util/Direction;Z)V");
                
                var newInstructions = new InsnList();
                newInstructions.add(new VarInsnNode(Opcodes.ALOAD, 1));
                newInstructions.add(new VarInsnNode(Opcodes.ALOAD, 2));
                newInstructions.add(new VarInsnNode(Opcodes.ALOAD, 4));
                newInstructions.add(new InsnNode(Opcodes.ICONST_1));
                newInstructions.add(ASM.buildMethodCall(
                    "vazkii/quark/base/handler/AsmHooks", 
                    "transformStructureHelper",
                    "(Lnet/minecraft/block/state/PistonBlockStructureHelper;Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/util/Direction;Z)Lnet/minecraft/block/state/PistonBlockStructureHelper;", 
                    ASM.MethodType.STATIC
                ));

                method.instructions.insert(target, newInstructions);

                return method;
            }
        }
    }
}
