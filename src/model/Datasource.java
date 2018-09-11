package model;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Datasource {
    public static final String DATABASE_NAME = "usertasks.db";
    public static final String CONNECTION_URL = "jdbc:sqlite:C:\\Users\\szerlag\\Desktop\\Programowanie\\Projekty\\INTELIJ\\tasksplanner\\" + DATABASE_NAME;

    public static final String TABLE_USERS = "users";
    public static final String COLUMN_USERS_ID = "id";
    public static final String COLUMN_USERS_NAME = "name";
    public static final String COLUMN_USERS_SURNAME = "surname";
    public static final int INDEX_USERS_ID = 1;
    public static final int INDEX_USERS_NAME = 2;
    public static final int INDEX_USERS_SURNAME = 3;

    public static final String TABLE_TASKS = "tasks";
    public static final String COLUMN_TASKS_ID = "id";
    public static final String COLUMN_TASKS_DESCRIPTION = "descrpition";
    public static final String COLUMN_TASKS_DONE = "task_done";
    public static final String COLUMN_TASKS_CREATE_DATE = "create_date";
    public static final String COLUMN_TASK_USER = "user";
    public static final int INDEX_TASK_ID = 1;
    public static final int INDEX_TASK_DESCRIPTION = 2;
    public static final int INDEX_TASK_DONE = 3;
    public static final int INDEX_TASK_CREATE_DATE = 4;
    public static final int INDEX_TASK_USER = 5;

    public static final String CREATE_USERS_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_USERS + "( " + COLUMN_USERS_ID + " integer PRIMARY KEY , " + COLUMN_USERS_NAME + " text , " + COLUMN_USERS_SURNAME + " text " + ")";
    public static final String CREATE_TASKS_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_TASKS + "(" + COLUMN_TASKS_ID + " integer PRIMARY KEY , " + COLUMN_TASKS_DESCRIPTION + " text , " + COLUMN_TASKS_DONE + " integer ," + COLUMN_TASKS_CREATE_DATE + " TIMESTAMP ," + COLUMN_TASK_USER + " integer )";

    public static final String INSERT_USER = "INSERT INTO " + TABLE_USERS + "( " + COLUMN_USERS_NAME + ", " + COLUMN_USERS_SURNAME + ") " + " VALUES(?, ?)";
    public static final String INSERT_TASK = "INSERT INTO " + TABLE_TASKS + "( " + COLUMN_TASKS_DESCRIPTION + ", " + COLUMN_TASKS_DONE + ", " + COLUMN_TASKS_CREATE_DATE + ", " + COLUMN_TASK_USER + ") " + " VALUES(?, ?, ?, ?)";

    public static final String QUERY_USER = "SELECT * FROM " + TABLE_USERS + " WHERE " + TABLE_USERS + "." + COLUMN_USERS_NAME + " = ?  " + " AND " + TABLE_USERS + "." + COLUMN_USERS_SURNAME + " =  ? ";

    public static final String TASK_USER_VIEW = "task_user_view";

    public static final String CREATE_TASK_USER_VIEW = "CREATE VIEW IF NOT EXISTS " + TASK_USER_VIEW + " AS SELECT " + TABLE_TASKS + "." + COLUMN_TASKS_ID + " , " + COLUMN_TASKS_DESCRIPTION + ", " + COLUMN_TASKS_DONE + ", " + COLUMN_TASKS_CREATE_DATE + ", " + TABLE_TASKS + "." + COLUMN_TASK_USER + ", " + TABLE_USERS + "." + COLUMN_USERS_NAME + ", " + TABLE_USERS + "." + COLUMN_USERS_SURNAME + " FROM " + TABLE_TASKS + " INNER JOIN " + TABLE_USERS + " ON " + TABLE_TASKS + "." + COLUMN_TASK_USER + " = " + TABLE_USERS + "." + COLUMN_USERS_ID;
    //+ " WHERE " + TABLE_USERS + "." + COLUMN_USERS_NAME + " = ?  " + " AND " + TABLE_USERS + "." + COLUMN_USERS_SURNAME + " =  ? ";
    public static final String QUERY_USER_TASKS = "SELECT * FROM " + TASK_USER_VIEW + " WHERE " + TASK_USER_VIEW + "." + COLUMN_USERS_NAME + " = ? AND " + TASK_USER_VIEW + "." + COLUMN_USERS_SURNAME + " = ?";

    private Connection conn;
    private PreparedStatement createUsersTable;
    private PreparedStatement createTaskTable;
    private PreparedStatement insertIntoUser;
    private PreparedStatement insertIntoTask;
    private PreparedStatement queryFromUser;
    private PreparedStatement createTaskUserView;
    private PreparedStatement queryUserTasks;

    private static Datasource instance = new Datasource();

    private Datasource(){

    }

    public static Datasource getInstance() {
        return instance;
    }

    //TODO add connection to database
    //Open connection with database
    public boolean open() {
        try {
            conn = DriverManager.getConnection(CONNECTION_URL);
            createUsersTable = conn.prepareStatement(CREATE_USERS_TABLE);
            createTaskTable = conn.prepareStatement(CREATE_TASKS_TABLE);
            createUsersTable.execute();
            createTaskTable.execute();
            insertIntoUser = conn.prepareStatement(INSERT_USER, Statement.RETURN_GENERATED_KEYS);
            insertIntoTask = conn.prepareStatement(INSERT_TASK, Statement.RETURN_GENERATED_KEYS);
            queryFromUser = conn.prepareStatement(QUERY_USER);
            createTaskUserView = conn.prepareStatement(CREATE_TASK_USER_VIEW);
            queryUserTasks = conn.prepareStatement(QUERY_USER_TASKS);
            System.out.println("Connection successful with DB");
            return true;
        } catch (SQLException e) {
            System.out.println("Couldn't connect with DB " + e.getMessage());
            e.printStackTrace();
            return false;
        }

    }

    public void close() {
        try {
            if (conn != null)
                conn.close();

        } catch (SQLException e) {
            System.out.println("Couldn't close DB " + e.getMessage());
        }
    }
    //TODO insertUser
    //Insert User to Database, returning ID of created user
    public int insertUser(String name, String surname) {
        try {
            insertIntoUser.setString(1, name);
            insertIntoUser.setString(2, surname);
            int affectedrows = insertIntoUser.executeUpdate();
            if (affectedrows != 1)
                throw new SQLException("Couldn't insert User");


            ResultSet generatedkeys = insertIntoUser.getGeneratedKeys();
            if (generatedkeys.next()) {
                insertIntoUser.setInt(1, generatedkeys.getInt(1));
            } else throw new SQLException("Couldn't insert User");
            return generatedkeys.getInt(1);

        } catch (SQLException e) {
            System.out.println("Couldn't INSERT INTO User " + e.getMessage());
            return 0;
        }
    }
    //TODO queryUser
    //TODO return User object, not int!
    //Query User, returning User object
    public User queryUser(String name, String surname) {
        try {
            User tempUser = new User();
            queryFromUser.setString(1, name);
            queryFromUser.setString(2, surname);
            ResultSet userResult = queryFromUser.executeQuery();
            if (userResult.next()) {
                tempUser.set_id(userResult.getInt(INDEX_USERS_ID));
                tempUser.setName(userResult.getString(INDEX_USERS_NAME));
                tempUser.setSurname(userResult.getString(INDEX_USERS_SURNAME));
                System.out.println("User found!\t" + tempUser.get_id() + " " + tempUser.getName() + " " + tempUser.getSurname());
            }
            return tempUser;
        } catch (SQLException e) {
            System.out.println("Couldn't find User");
            return null;

        }
    }

    //TODO insertTask
    //Creating task
    public int insertTask(String name, String surname, String description, Boolean taskDone) {

        try {
            User foundedUser = queryUser(name, surname);
            if (foundedUser != null) {
                insertIntoTask.setString(1, description);
                insertIntoTask.setBoolean(2, taskDone);
                insertIntoTask.setTimestamp(3, new Timestamp(new java.util.Date().getTime()));
                insertIntoTask.setInt(4, foundedUser.get_id());
                insertIntoTask.execute();
                System.out.println("Task Added!");
            }
            ResultSet resultTask = insertIntoTask.getGeneratedKeys();
            if (resultTask != null)
                return resultTask.getInt(1);

        } catch (SQLException e) {
            System.out.println("Couldn't add task! " + e.getMessage());
        }
        return 0;

    }


    //TODO createView with possibility to updating
    //Creating View with INNER JOIN USER - TASKS
    public void createView() {
        try {
            createTaskUserView.execute();
        } catch (SQLException e) {
            System.out.println("Sorry, couldn't create view" + e.getMessage());
        }
    }

    //TODO query Tasks for selected user
    //TODO fixing parser for date format
    //Query user Tasks, returning List with user tasks
    public List<Task> queryTasks(User user) throws SQLException {
        List<Task> tempTasks = new ArrayList<>();
        if (user != null) {
            try {
                queryUserTasks.setString(1, user.getName());
                queryUserTasks.setString(2, user.getSurname());
                ResultSet resultTasks = queryUserTasks.executeQuery();
                while (resultTasks.next()) {
                    Task tempTask = new Task();
                    tempTask.set_id(resultTasks.getInt(COLUMN_TASKS_ID));
                    tempTask.setDescription(resultTasks.getString(COLUMN_TASKS_DESCRIPTION));
                    tempTask.setCreateDate(resultTasks.getTimestamp(COLUMN_TASKS_CREATE_DATE));
                    tempTask.setTaskDone(resultTasks.getBoolean(COLUMN_TASKS_DONE));
                    tempTask.setUser(resultTasks.getInt(COLUMN_TASK_USER));
                    tempTasks.add(tempTask);
                }
                return tempTasks;
            } catch (SQLException e) {
                System.out.println("Sorry, can't query tasks " + e.getMessage());
                System.out.println(e.getStackTrace());
                return null;

            }
        } else {
            throw new SQLException("Sorry, user doesn't exist in database");
        }

    }

    //TODO updateStatus give opportunity to change TASK_DONE
    //TODO queryTask to list


}
