function remapBlockState(method) {
    var ASM = Java.type('net.minecraftforge.coremod.api.ASMAPI');

    var target = ASM.findFirstMethodCall(method,
        ASM.MethodType.VIRTUAL,
        "net/minecraft/world/World",
        ASM.mapMethod("setBlockState"),
        "(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;I)Z");

    while (target !== null) {
        var index = method.instructions.indexOf(target);
        method.instructions.insert(target, ASM.buildMethodCall(
            "vazkii/quark/base/handler/AsmHooks",
            "setPistonBlock",
            "(Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;I)Z",
            ASM.MethodType.STATIC
        ));
        method.instructions.remove(target);

        target = ASM.findFirstMethodCallAfter(method,
            ASM.MethodType.VIRTUAL,
            "net/minecraft/world/World",
            ASM.mapMethod("setBlockState"),
            "(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;I)Z",
            index);
    }

    return method;
}

function initializeCoreMod() {
    return {
        'piston-update-hook': {
            'target': {
                'type': 'METHOD',
                'class': 'net.minecraft.tileentity.PistonTileEntity',
                'methodName': 'tick',
                'methodDesc': '()V'
            },
            'transformer': function(method) {
                var ASM = Java.type('net.minecraftforge.coremod.api.ASMAPI');
                var Opcodes = Java.type('org.objectweb.asm.Opcodes');
                var VarInsnNode = Java.type('org.objectweb.asm.tree.VarInsnNode');
                var InsnList = Java.type('org.objectweb.asm.tree.InsnList');

                var newInstructions = new InsnList();
                newInstructions.add(new VarInsnNode(Opcodes.ALOAD, 0));
                newInstructions.add(ASM.buildMethodCall(
                    "vazkii/quark/base/handler/AsmHooks", 
                    "onPistonUpdate",
                    "(Lnet/minecraft/tileentity/PistonTileEntity;)V",
                    ASM.MethodType.STATIC
                ));

                method.instructions.insertBefore(method.instructions.getFirst(), newInstructions);

                return remapBlockState(method);
            }
        },

        'clear-piston-data': {
            'target': {
                'type': 'METHOD',
                'class': 'net.minecraft.block.PistonBlock',
                'methodName': 'clearPistonTileEntity',
                'methodDesc': '()V'
            },
            'transformer': remapBlockState
        },

        'add-renderer-hook': {
            'target': {
                'type': 'METHOD',
                'class': 'net.minecraft.client.renderer.tileentity.PistonTileEntityRenderer',
                'methodName': 'render',
                'methodDesc': '(Lnet/minecraft/tileentity/PistonTileEntity;DDDFI)V'
            },
            'transformer': function(method) {
                var ASM = Java.type('net.minecraftforge.coremod.api.ASMAPI');
                var Opcodes = Java.type('org.objectweb.asm.Opcodes');
                var VarInsnNode = Java.type('org.objectweb.asm.tree.VarInsnNode');
                var JumpInsnNode = Java.type('org.objectweb.asm.tree.JumpInsnNode');
                var InsnNode = Java.type('org.objectweb.asm.tree.InsnNode');
                var LabelNode = Java.type('org.objectweb.asm.tree.LabelNode');
                var InsnList = Java.type('org.objectweb.asm.tree.InsnList');

                var newInstructions = new InsnList();

                var escape = new LabelNode();

                newInstructions.add(new VarInsnNode(Opcodes.ALOAD, 1));
                newInstructions.add(new VarInsnNode(Opcodes.DLOAD, 2));
                newInstructions.add(new VarInsnNode(Opcodes.DLOAD, 4));
                newInstructions.add(new VarInsnNode(Opcodes.DLOAD, 6));
                newInstructions.add(new VarInsnNode(Opcodes.FLOAD, 8));
                newInstructions.add(ASM.buildMethodCall(
                    "vazkii/quark/base/handler/AsmHooks",
                    "renderPistonBlock",
                    "(Lnet/minecraft/tileentity/PistonTileEntity;DDDF)Z",
                    ASM.MethodType.STATIC));
                newInstructions.add(new JumpInsnNode(Opcodes.IFEQ, escape));
                newInstructions.add(new InsnNode(Opcodes.RETURN));
                newInstructions.add(escape);

                method.instructions.insertBefore(method.instructions.getFirst(), newInstructions);
                return method;
            }
        }
    }
}
