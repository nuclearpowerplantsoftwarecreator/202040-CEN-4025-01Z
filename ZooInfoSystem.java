// Filename: ZooInfoSystem.java
// Author: Bravo, Jose
// Date: 25 October 2020

// Future improvements:
// - prevent overtime
// - accommodate minors working limitations
// - import/export data
// - prevent removal of employees with assigned tasks
// - checking in/checking out tasks
// - store table data into SQL (public location) using JDBC

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;

public class ZooInfoSystem
{
    // employee table in place of database
    // define size of multidimentional array
    static int MAX_EMP_COUNT = 255; //rows
    static int EMP_TABLE_FIELDS = 5; //columns
    
    // define fields in array
    static int EMP_NAME = 0;
    static int EMP_START_HOUR = 1;
    static int EMP_START_MIN = 2;
    static int EMP_END_HOUR = 3;
    static int EMP_END_MIN = 4;
    
    // define field headers and custom widths
    // defautl width is length of header
    static String EMP_HEADERS[] =
    {
        "Employee #",
        "Employee Name",
        "Shift Start HR",
        "Shift Start MN",
        "Shift End HR", 
        "Shift End HR"
    };
    
    static int EMP_TABLE_COL_WIDTH = 20;
    
    // valid field boundaries
    // can be modified to accommodate working hours
    // default is 24 hours
    static int EMP_SHIFT_START_TIME_HOUR = 0;
    static int EMP_SHIFT_START_TIME_MIN = 0;
    static int EMP_SHIFT_END_TIME_HOUR = 23;
    static int EMP_SHIFT_END_TIME_MIN = 59;
    
    // define array
    public static String employeeTable[][] = new String[MAX_EMP_COUNT][EMP_TABLE_FIELDS];
    
    // define table output format
    public static String EMP_TABLE_FORMAT = "%-" + EMP_TABLE_COL_WIDTH + "s ";
    
    // array index tracking
    public static int currentEmployeeCount = 0;
    
    // task table in place of database
    // define size of multidimentional array
    static int MAX_TASK_COUNT = 255; //rows
    static int TASK_TABLE_FIELDS = 10; //columns
    
    // define fields in array
    static int TASK_ZONE = 0;
    static int TASK_TYPE = 1;
    static int TASK_HOUR = 2;
    static int TASK_MIN = 3;
    static int TASK_DURATION_MIN = 4;
    static int TASK_ASSIGNED_EMP_ID = 5;
    static int TASK_START_TIME_HOUR = 6;
    static int TASK_START_TIME_MINE = 7;
    static int TASK_END_TIME_HOUR = 8;
    static int TASK_END_TIME_MIN = 9;

    // table headers
    static String[] TASK_HEADERS = 
    {
            "Task ID",
            "Task Zone",
            "Task Type",
            "Task Hour",
            "Task Min",
            "Task Duration (mins)",
            "Assigned Employee ID",
            "Task Start Hour",
            "Task Start Minute",
            "Task End Hour",
            "Task End Minute"
    };
    
    static String[] TASK_ZONES =
    {
        "Expedition Africa",
        "Flamigo Cafe",
        "Wild Florida",
        "La Selva Rainforest",
        "Paws On",
        "Bonsai Exhibit"
    };
    
    static String[] TASK_TYPES =
    {
        "Food / Feeding",
        "Groundskeeping",
        "Trash Removal",
        "General Maintenance"
    };
    
    // column width for task table
    static int TASK_TABLE_COL_WIDTH = 24;
    
    // valid field boundries
    // can be modified to accommodate working hours
    // default is 24 hours
    static int TASK_SHIFT_START_TIME_HOUR = 0;
    static int TASK_SHIFT_START_TIME_MIN = 0;
    static int TASK_SHIFT_END_TIME_HOUR = 23;
    static int TASK_SHIFT_END_TIME_MIN = 59;

    // define array
    static String taskTable[][] = new String[MAX_TASK_COUNT][TASK_TABLE_FIELDS];
    
    // define table output format
    public static String TASK_TABLE_FORMAT = "%-" + TASK_TABLE_COL_WIDTH + "s ";
    
    // array index tracking
    public static int currentTaskCount = 0;
    
    // create other global variables
    public static String format = "";
    public static String userInput = "";
    
    // create Scanner instance to read user input
    public static Scanner sc = new Scanner(System.in);
    
    // utility functions
    public static boolean doesExist(int count, String item)
    {
        if(count <= 0)
        {
            System.out.println("No current " + item + " configured.");
            pressEnterToContinue();
        }
        
        return count > 0;
    }

    public static void outputTable (int rStart, int rEnd, String oFormat, String headers[], String table[][])
    {
        // output headers
        for(int row = rStart; row < headers.length; row++)
            System.out.printf(oFormat, headers[row]);

        System.out.println();
        
        // output table data
        for(int row = rStart; row < rEnd; row++)
        {
            for(int col = 0; col < table[row].length; col++)
            {
                if(col == 0)
                    System.out.printf(oFormat, String.valueOf(row + 1));

                if(table[row][col] == null)
                    System.out.printf(oFormat, "-----");
                
                else
                    System.out.printf(oFormat, table[row][col]);
            }
            
            System.out.println();
        }
        
        return;
    }

    public static int determineValue(int a, int z, String s)
    {
        int eval;
        boolean valid = false;
        
        do
        {
            System.out.printf("Enter the %s (%d-%d):", s, a, z);
            eval = sc.nextInt();
            
            valid = (eval >= a && eval <= z);

            if(!valid)
                System.out.printf("Enter a valid value (%d-%d).\n", a, z);
            
        } while (!valid);
        
        return eval;
    }

    public static void showEmployees()
    {
        if(!doesExist(currentEmployeeCount, "employees"))
            return;
        
        System.out.println("Current listed employee information:");
        System.out.println();
        
        outputTable(0, currentEmployeeCount, EMP_TABLE_FORMAT, EMP_HEADERS, employeeTable);
        
        pressEnterToContinue();
        return;
    }
    
    public static void addEmployee()
    {
        System.out.println("Adding new employee.");
        System.out.println();
        
        System.out.print("Enter a first name (no spaces): ");
        String fn = sc.next();
        
        System.out.print("Enter a last name (no spaces): ");
        String ln = sc.next();
        String name = fn + " " + ln;

        int hourStart = determineValue(EMP_SHIFT_START_TIME_HOUR, EMP_SHIFT_END_TIME_HOUR, "start hour of the shift");
        int minStart = determineValue(EMP_SHIFT_START_TIME_MIN, EMP_SHIFT_END_TIME_MIN, "start minute of the shift");
        System.out.println("NOTE: The end time cannot be prior to the start time of the shift");
        int hourEnd =  determineValue(hourStart, EMP_SHIFT_END_TIME_HOUR, "ending hour of the shift");
        int minEnd = determineValue(minStart, EMP_SHIFT_END_TIME_MIN, "start minute of the shift");

        employeeTable[currentEmployeeCount][EMP_NAME] = name;
        employeeTable[currentEmployeeCount][EMP_START_HOUR] = new Integer(hourStart).toString();
        employeeTable[currentEmployeeCount][EMP_START_MIN] = new Integer(minStart).toString();
        employeeTable[currentEmployeeCount][EMP_END_HOUR] = new Integer(hourEnd).toString();
        employeeTable[currentEmployeeCount][EMP_END_MIN] = new Integer(minEnd).toString();
        
        currentEmployeeCount++;
        System.out.println("Employee added.");
        pressEnterToContinue();
        return;
    }
    
    public static void modifyEmployee()
    {
        if(!doesExist(currentEmployeeCount, "employees"))
            return;
        
        System.out.println("Current listed employee information:");
        System.out.println();
        
        outputTable(0, currentEmployeeCount, EMP_TABLE_FORMAT, EMP_HEADERS, employeeTable);
        
        boolean valid = false;
        int modifyEmpID = 0;
        
        do
        {
            System.out.print("Press enter an employee ID to modify: ");
            modifyEmpID = sc.nextInt();
            
            valid = (modifyEmpID >= 1 && modifyEmpID <= currentEmployeeCount);
            
            if(!valid)
                System.out.println("Please enter a valid value.");

        } while(!valid);
        
        modifyEmpID--;
        
        System.out.println("Modifying the following employee entry:");
        System.out.println();
        
        outputTable(modifyEmpID, modifyEmpID + 1, EMP_TABLE_FORMAT, EMP_HEADERS, employeeTable);
        System.out.println();
        
        System.out.print("Enter a first name (no spaces): ");
        String fn = sc.next();
        
        System.out.print("Enter a last name (no spaces): ");
        String ln = sc.next();
        String name = fn + " " + ln;

        int hourStart = determineValue(EMP_SHIFT_START_TIME_HOUR, EMP_SHIFT_END_TIME_HOUR, "start hour of the shift");
        int minStart = determineValue(EMP_SHIFT_START_TIME_MIN, EMP_SHIFT_END_TIME_MIN, "start minute of the shift");
        System.out.println("NOTE: The end time cannot be prior to the start time of the shift");
        int hourEnd =  determineValue(hourStart, EMP_SHIFT_END_TIME_HOUR, "ending hour of the shift");
        int minEnd = determineValue(minStart, EMP_SHIFT_END_TIME_MIN, "start minute of the shift");
        
        employeeTable[modifyEmpID][EMP_NAME] = name;
        employeeTable[modifyEmpID][EMP_START_HOUR] = new Integer(hourStart).toString();
        employeeTable[modifyEmpID][EMP_START_MIN] = new Integer(minStart).toString();
        employeeTable[modifyEmpID][EMP_END_HOUR] = new Integer(hourEnd).toString();
        employeeTable[modifyEmpID][EMP_END_MIN] = new Integer(minEnd).toString();
        
        System.out.println("Employee modified.");
        pressEnterToContinue();
        return;
    }
    
    public static void deleteEmployee()
    {
        if(!doesExist(currentEmployeeCount, "employees"))
            return;
        
        outputTable(0, currentEmployeeCount, EMP_TABLE_FORMAT, EMP_HEADERS, employeeTable);
        
        System.out.print("Enter an employee ID to remove: ");
        int deleteEmpID = sc.nextInt();
        
        deleteEmpID--;
        
        for(int row = deleteEmpID; row < currentEmployeeCount; row++)
        {
            for(int col = 0; col < employeeTable[row].length; col++)
            {
                employeeTable[row][col] = employeeTable[row + 1][col];
            }
        }
        
        currentEmployeeCount--;
        System.out.println("Employee deleted.");
        pressEnterToContinue();
        return;
    }
    
    public static void showTasks(String t[][])
    {
        if(!doesExist(currentTaskCount, "tasks"))
            return;
        
        System.out.println("Current listed task information:");
        System.out.println();
        
        outputTable(0, currentTaskCount, TASK_TABLE_FORMAT, TASK_HEADERS, taskTable);
        
        pressEnterToContinue();
        return;
    }

    public static void addTask()
    {
        String zone;
        String type;
        int taskHour;
        int taskMin;
        int taskDuration;
        
        boolean valid = false;
        int choice;

        String format = "%-" + TASK_TABLE_COL_WIDTH + "s %-" + TASK_TABLE_COL_WIDTH + "s\n";
        
        System.out.println("Adding new task.");
        System.out.println();

        System.out.println("List of areas:");
        for(int row = 0; row < TASK_ZONES.length; row++)
        {
            if(row == 0)
                    System.out.printf(format, "Area #", "Area Description");
                
            System.out.printf(format, String.valueOf(row + 1), TASK_ZONES[row]);
        }
        System.out.println();

        do
        {
            System.out.print("Select an area number: ");
            choice = sc.nextInt();
            
            valid = (choice >= 1 && choice <= TASK_ZONES.length);
            
            if(!valid)
                System.out.println("Enter a valid choice.");
            
        } while(!valid);
        
        zone = TASK_ZONES[choice - 1];
        
        valid = false;
        
        System.out.println("List of task types:");
        for(int row = 0; row < TASK_TYPES.length; row++)
        {
            if(row == 0)
            {
                System.out.printf(format, "Task Type #", "Task Type Description");
            }
            
            System.out.printf(format, String.valueOf(row + 1), TASK_TYPES[row]);
        }
        
        do
        {
            System.out.print("Select an task type number: ");
            choice = sc.nextInt();
            
            valid = (choice >= 1 && choice <= TASK_TYPES.length);
            
            if(!valid)
                System.out.println("Enter a valid choice.");
            
        } while(!valid);
        
        type = TASK_TYPES[choice - 1];
        
        taskHour = determineValue(0, 23, "start hour of the task");
        taskMin = determineValue(0, 59, "start minute of the task");
        
        valid = false;
        System.out.println("NOTE: Task duration cannot run passed 23:59.");
        
        do
        {
            System.out.print("Enter the task duration: ");
            taskDuration = sc.nextInt();

            int divH = taskHour + taskDuration / 60;
            int modM = taskMin + taskDuration % 60;
            
            valid = (divH <= 23 && modM <= 59);
            
            if(!valid)
                System.out.println("Enter a valid value.");
            
        } while (!valid);

        taskTable[currentTaskCount][TASK_ZONE] = zone;
        taskTable[currentTaskCount][TASK_TYPE] = type;
        taskTable[currentTaskCount][TASK_HOUR] = new Integer(taskHour).toString();
        taskTable[currentTaskCount][TASK_MIN] = new Integer(taskMin).toString();
        taskTable[currentTaskCount][TASK_DURATION_MIN] = new Integer(taskDuration).toString();
                
        currentTaskCount++;
        System.out.println("Task added.");
        pressEnterToContinue();
        return;
    }
    
    public static void modifyTask()
    {
        if(!doesExist(currentTaskCount, "tasks"))
            return;
        
        System.out.println("Current listed task information:");
        System.out.println();
        
        outputTable(0, currentTaskCount, TASK_TABLE_FORMAT, TASK_HEADERS, taskTable);
        System.out.println();
        
        int choice;
        boolean valid = false;
        
        do
        {
            System.out.print("Enter a task # to modify: ");
            choice = sc.nextInt();
            
            valid = (choice > 0 && choice <= currentTaskCount);
            
            if(!valid)
                System.out.println("Please make a valid selection.");
            
        } while(!valid);
        
        choice--; // make choice equal to index value
        
        String zone;
        String type;
        int taskHour;
        int taskMin;
        int taskDuration;
        
        System.out.println("Modifying task.");
        System.out.println();

        System.out.println("Selected task:");
        outputTable(choice, choice + 1, TASK_TABLE_FORMAT, TASK_HEADERS, taskTable);
        System.out.println();
        
        valid = false;
        String format = "%-" + TASK_TABLE_COL_WIDTH + "s %-" + TASK_TABLE_COL_WIDTH + "s\n";
        
        System.out.println("List of areas:");
        for(int row = 0; row < TASK_ZONES.length; row++)
        {
            if(row == 0)
                    System.out.printf(format, "Area #", "Area Description");
                
            System.out.printf(format, String.valueOf(row + 1), TASK_ZONES[row]);
        }
        System.out.println();

        do
        {
            System.out.print("Select an area number: ");
            choice = sc.nextInt();
            
            valid = (choice >= 1 && choice <= TASK_ZONES.length);
            
            if(!valid)
                System.out.println("Enter a valid choice.");
            
        } while(!valid);
        
        zone = TASK_ZONES[choice - 1];
        
        valid = false;
        
        System.out.println("List of task types:");
        for(int row = 0; row < TASK_TYPES.length; row++)
        {
            if(row == 0)
            {
                System.out.printf(format, "Task Type #", "Task Type Description");
            }
            
            System.out.printf(format, String.valueOf(row + 1), TASK_TYPES[row]);
        }
        
        do
        {
            System.out.print("Select an task type number: ");
            choice = sc.nextInt();
            
            valid = (choice >= 1 && choice <= TASK_TYPES.length);
            
            if(!valid)
                System.out.println("Enter a valid choice.");
            
        } while(!valid);
        
        type = TASK_TYPES[choice - 1];
        
        taskHour = determineValue(0, 23, "start hour of the task");
        taskMin = determineValue(0, 59, "start minute of the task");
        
        valid = false;
        System.out.println("NOTE: Task duration cannot run passed 23:59.");
        
        do
        {
            System.out.print("Enter the task duration: ");
            taskDuration = sc.nextInt();

            valid = (taskHour + (taskDuration / 60) <= 23 && taskMin + (taskDuration % 60) <= 59);
            
            if(!valid)
                System.out.println("Enter a valid value.");

        } while (!valid);

        taskTable[currentTaskCount][TASK_ZONE] = zone;
        taskTable[currentTaskCount][TASK_TYPE] = type;
        taskTable[currentTaskCount][TASK_HOUR] = new Integer(taskHour).toString();
        taskTable[currentTaskCount][TASK_MIN] = new Integer(taskMin).toString();
        taskTable[currentTaskCount][TASK_DURATION_MIN] = new Integer(taskDuration).toString();
                
        System.out.println("Task modified.");
        pressEnterToContinue();
        return;
    }
    
    public static void deleteTask()
    {
        if(!doesExist(currentTaskCount, "tasks"))
            return;
        
        System.out.println("Current listed task information:");
        System.out.println();
        
        outputTable(0, currentTaskCount, TASK_TABLE_FORMAT, TASK_HEADERS, taskTable);
        System.out.println();
        
        int choice;
        boolean valid = false;
        
        do
        {
            System.out.print("Enter a task # to delete: ");
            choice = sc.nextInt();
            
            valid = (choice > 0 && choice <= currentTaskCount);
            
            if(!valid)
                System.out.println("Please make a valid selection.");
            
        } while(!valid);
        
        choice--; // make choice equal to index valu
        
        for(int row = choice; row < currentEmployeeCount; row++)
        {
            for(int col = 0; col < taskTable[row].length; col++)
            {
                taskTable[row][col] = taskTable[row + 1][col];
            }
        }
        System.out.println("Task deleted.");
        pressEnterToContinue();
        return;
    }
    
    public static void pressEnterToContinue()
    {
        System.out.println("Press enter to continue.");
        try { System.in.read(); }
        catch (IOException e1) { e1.printStackTrace(); }
        return;
    }
    
    public static void main(String[] args)
    {
        int menuChoice = -1;
                        
        do
        {
            System.out.println("*****************************");
            System.out.println("********** WELCOME **********");
            System.out.println("** Please make a selection **");
            System.out.println("*****************************");
            System.out.println();
            System.out.println("1:  List all employees");
            System.out.println("2:  Add an employee");
            System.out.println("3:  Modify an existing employee");
            System.out.println("4.  Delete an existing employee");
            System.out.println("5.  List all tasks");
            System.out.println("6.  Add a task");
            System.out.println("7.  Modify an existing task");
            System.out.println("8.  Delete an existing task");
            System.out.println("9.  Check out a task");
            System.out.println("10. Check in a task");
            System.out.println();
            System.out.println("0:  Exit program");
            System.out.println();
            System.out.println("*****************************");
            
            System.out.print("Enter a selection: ");
            menuChoice = sc.nextInt();

            switch(menuChoice)
            {
                case 0:
                    System.out.println("Exiting. Thank you for using the ZIS software.");
                    pressEnterToContinue();
                    sc.close();
                    break;
                    
                case 1:
                    showEmployees();
                    break;
                        
                case 2:
                    addEmployee();
                    break;
                    
                case 3:
                    modifyEmployee();
                    break;
                
                case 4:
                    deleteEmployee();
                    break;
                    
                case 5:
                    showTasks(taskTable);
                    break;
                    
                case 6:
                    addTask();
                    break;
                    
                case 7:
                    modifyTask();
                    break;
                    
                case 8:
                    deleteTask();
                    break;
                
                case 9:
                    // checkOutTask(taskTable)
                    // show tasks
                    // select task
                    // enter start hour
                    // enter start min
                    // show employees
                    // select employee
                    // update task table
                    System.out.println("To be added at a future time.");
                    pressEnterToContinue();
                    break;
                    
                case 10:
                    // checkInTask(taskTable)
                    // show tasks
                    // select task
                    // enter end hour
                    // enter end min
                    // update task table
                    System.out.println("To be added at a future time.");
                    pressEnterToContinue();
                    break;
                    
                default:
                    System.out.println("Enter a valid option.");
                    pressEnterToContinue();
                    break;
            }

        } while (menuChoice != 0);
    }
}
