package com.appdynamics.javacert.level2;

import com.appdynamics.javacert.level2.command.AddClientCommandHandler;
import com.appdynamics.javacert.level2.command.AddDeptCommandHandler;
import com.appdynamics.javacert.level2.command.AddEmployeeCommandHandler;
import com.appdynamics.javacert.level2.command.AddUserCommandHandler;
import com.appdynamics.javacert.level2.command.DeleteClientCommandHandler;
import com.appdynamics.javacert.level2.command.DeleteDeptCommandHandler;
import com.appdynamics.javacert.level2.command.DeleteUserCommandHandler;
import com.appdynamics.javacert.level2.command.UpdateEmployeeLevelCommandHandler;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by trader on 2/9/17.
 */
public class CommandDispatcher {

    private static final Map<String, CommandHandler> DISPATCH;

    static {
        DISPATCH = new HashMap<String, CommandHandler>();
        DISPATCH.put("addClient", new AddClientCommandHandler());
        DISPATCH.put("addDept", new AddDeptCommandHandler());
        DISPATCH.put("addEmployee", new AddEmployeeCommandHandler());
        DISPATCH.put("addUser", new AddUserCommandHandler());
        DISPATCH.put("deleteClient", new DeleteClientCommandHandler());
        DISPATCH.put("deleteDept", new DeleteDeptCommandHandler());
        DISPATCH.put("deleteUser", new DeleteUserCommandHandler());
        DISPATCH.put("updateEmployeeLevel", new UpdateEmployeeLevelCommandHandler());
    }

    public static CommandHandler forCommand(String command) {
        return DISPATCH.get(command);
    }
}
