function injectForEachMethod(method, targetType, clazz, targetName, sig, callback) {
    var ASM = Java.type('net.minecraftforge.coremod.api.ASMAPI');

    var target = ASM.findFirstMethodCall(method,
        targetType,
        clazz,
        targetName,
        sig);

    while (target !== null) {
        var index = method.instructions.indexOf(target);
        var indexShift = callback(target, index);

        var newIndex = method.instructions.indexOf(target);
        if (newIndex !== -1)
            index = newIndex;
        else if (typeof indexShift === 'number')
            index += indexShift;

        target = ASM.findFirstMethodCallAfter(method,
            targetType,
            clazz,
            targetName,
            sig,
            index + 1);
    }

    return method;
}

function initializeCoreMod() {
    return {
        'extract-color': {
            'target': {
                'type': 'METHOD',
                'class': 'net.minecraft.client.renderer.ItemRenderer',
                'methodName': 'func_229111_a_', // renderItem
                'methodDesc': '(Lnet/minecraft/item/ItemStack;Lnet/minecraft/client/renderer/model/ItemCameraTransforms$TransformType;ZLcom/mojang/blaze3d/matrix/MatrixStack;Lnet/minecraft/client/renderer/IRenderTypeBuffer;IILnet/minecraft/client/renderer/model/IBakedModel;)V'
            },
            'transformer': function(method) {
                var ASM = Java.type('net.minecraftforge.coremod.api.ASMAPI');
                var Opcodes = Java.type('org.objectweb.asm.Opcodes');
                var VarInsnNode = Java.type('org.objectweb.asm.tree.VarInsnNode');
                var InsnList = Java.type('org.objectweb.asm.tree.InsnList');

                var newInstructions = new InsnList();
                newInstructions.add(new VarInsnNode(Opcodes.ALOAD, 1));
                newInstructions.add(ASM.buildMethodCall(
                    "vazkii/quark/base/handler/AsmHooks", 
                    "setColorRuneTargetStack",
                    "(Lnet/minecraft/item/ItemStack;)V", 
                    ASM.MethodType.STATIC
                ));

                method.instructions.insertBefore(method.instructions.getFirst(), newInstructions);

                return method;
            }
        },

        'change-glint-render': {
            'target': {
                'type': 'METHOD',
                'class': 'net.minecraft.client.renderer.RenderType',
                'methodName': 'func_228653_j_', // getGlint
                'methodDesc': '()Lnet/minecraft/client/renderer/RenderType;'
            },
            'transformer': function(method) {
                var ASM = Java.type('net.minecraftforge.coremod.api.ASMAPI');
                var Opcodes = Java.type('org.objectweb.asm.Opcodes');
                var VarInsnNode = Java.type('org.objectweb.asm.tree.VarInsnNode');
                var InsnList = Java.type('org.objectweb.asm.tree.InsnList');

                var newInstructions = new InsnList();
                newInstructions.add(ASM.buildMethodCall(
                    "vazkii/quark/base/handler/AsmHooks", 
                    "getGlintRender",
                    "(Lnet/minecraft/client/renderer/RenderType;)Lnet/minecraft/client/renderer/RenderType;", 
                    ASM.MethodType.STATIC
                ));

                method.instructions.insertBefore(method.instructions.getLast(), newInstructions);

                return method;
            }
        },

        'change-entity-glint-render': {
            'target': {
                'type': 'METHOD',
                'class': 'net.minecraft.client.renderer.RenderType',
                'methodName': 'func_228655_k_', // getEntityGlint
                'methodDesc': '()Lnet/minecraft/client/renderer/RenderType;'
            },
            'transformer': function(method) {
                var ASM = Java.type('net.minecraftforge.coremod.api.ASMAPI');
                var Opcodes = Java.type('org.objectweb.asm.Opcodes');
                var VarInsnNode = Java.type('org.objectweb.asm.tree.VarInsnNode');
                var InsnList = Java.type('org.objectweb.asm.tree.InsnList');

                var newInstructions = new InsnList();
                newInstructions.add(ASM.buildMethodCall(
                    "vazkii/quark/base/handler/AsmHooks", 
                    "getEntityGlintRender",
                    "(Lnet/minecraft/client/renderer/RenderType;)Lnet/minecraft/client/renderer/RenderType;", 
                    ASM.MethodType.STATIC
                ));

                method.instructions.insertBefore(method.instructions.getLast(), newInstructions);

                return method;
            }
        },

        'extract-elytra-color': {
            'target': {
                'type': 'METHOD',
                'class': 'net.minecraft.client.renderer.entity.layers.ElytraLayer',
                'methodName': 'func_225628_a_', // render
                'methodDesc': '(Lcom/mojang/blaze3d/matrix/MatrixStack;Lcom/mojang/blaze3d/vertex/IVertexBuilder;IIFFFF)V'
            },
            'transformer': function(method) {
                var ASM = Java.type('net.minecraftforge.coremod.api.ASMAPI');
                var Opcodes = Java.type('org.objectweb.asm.Opcodes');
                var VarInsnNode = Java.type('org.objectweb.asm.tree.VarInsnNode');
                var InsnList = Java.type('org.objectweb.asm.tree.InsnList');

                return injectForEachMethod(method,
                    ASM.MethodType.VIRTUAL,
                    "com/mojang/blaze3d/matrix/MatrixStack",
                    ASM.mapMethod("func_227860_a_"), // push
                    "()V",
                    function (target) {
                        var newInstructions = new InsnList();
                        newInstructions.add(new VarInsnNode(Opcodes.ALOAD, 11));
                        newInstructions.add(ASM.buildMethodCall(
                            "vazkii/quark/base/handler/AsmHooks",
                            "setColorRuneTargetStack",
                            "(Lnet/minecraft/item/ItemStack;)V",
                            ASM.MethodType.STATIC
                        ));

                        method.instructions.insertBefore(target, newInstructions);
                    });
            }
        },

        'extract-armor-color': {
            'target': {
                'type': 'METHOD',
                'class': 'net.minecraft.client.renderer.entity.layers.ArmorLayer',
                'methodName': 'func_229129_a_', // renderArmorPart
                'methodDesc': '(Lcom/mojang/blaze3d/matrix/MatrixStack;Lnet/minecraft/client/renderer/IRenderTypeBuffer;Lnet/minecraft/entity/LivingEntity;FFFFFFLnet/minecraft/inventory/EquipmentSlotType;I)V'
            },
            'transformer': function(method) {
                var ASM = Java.type('net.minecraftforge.coremod.api.ASMAPI');
                var Opcodes = Java.type('org.objectweb.asm.Opcodes');
                var VarInsnNode = Java.type('org.objectweb.asm.tree.VarInsnNode');
                var InsnList = Java.type('org.objectweb.asm.tree.InsnList');

                var newInstructions = new InsnList();
                return injectForEachMethod(method,
                    ASM.MethodType.VIRTUAL,
                    "net/minecraft/client/renderer/entity/layers/ArmorLayer",
                    ASM.mapMethod("func_215337_a"), // getModelFromSlot
                    "(Lnet/minecraft/inventory/EquipmentSlotType;)Lnet/minecraft/client/renderer/entity/model/BipedModel;",
                    function (target) {
                        var newInstructions = new InsnList();
                        newInstructions.add(new VarInsnNode(Opcodes.ALOAD, 12));
                        newInstructions.add(ASM.buildMethodCall(
                            "vazkii/quark/base/handler/AsmHooks",
                            "setColorRuneTargetStack",
                            "(Lnet/minecraft/item/ItemStack;)V",
                            ASM.MethodType.STATIC
                        ));

                        method.instructions.insertBefore(target, newInstructions);
                    });

                return method;
            }
        }
    }
}
