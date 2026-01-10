package eab.locateo.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.LiteralArgumentBuilder; // [新增] 必须导入这个类
import com.mojang.brigadier.tree.CommandNode;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;

public class LocateoCommand {
    
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        // 1. 获取原版 /locate 命令的根节点
        CommandNode<CommandSourceStack> vanillaLocateNode = dispatcher.getRoot().getChild("locate");

        if (vanillaLocateNode != null) {
            // 2. 创建 /locateo 命令头
            // [修复] 这里必须明确类型为 LiteralArgumentBuilder，否则 dispatcher.register 会报错
            LiteralArgumentBuilder<CommandSourceStack> builder = Commands.literal("locateo")
                .requires(source -> true); // 强制设为所有人可用

            // 3. 深度复制所有子节点并剥离权限锁
            for (CommandNode<CommandSourceStack> child : vanillaLocateNode.getChildren()) {
                builder.then(copyWithoutRequirements(child));
            }

            // 4. 注册
            dispatcher.register(builder);
            
            System.out.println("Locateo: Successfully mirrored /locate command for non-OPs");
        } else {
            System.err.println("Locateo Error: Failed to find vanilla /locate command!");
        }
    }

    /**
     * 递归复制命令节点，并将权限要求强制设为 true
     */
    private static <S> CommandNode<S> copyWithoutRequirements(CommandNode<S> source) {
        // 从源节点创建一个新的构建器
        ArgumentBuilder<S, ?> builder = source.createBuilder();
        
        // 覆盖权限检查
        builder.requires(s -> true);

        // 递归处理子节点
        for (CommandNode<S> child : source.getChildren()) {
            // .then() 可以接受 CommandNode，所以这里递归调用没问题
            builder.then(copyWithoutRequirements(child));
        }

        return builder.build();
    }
}