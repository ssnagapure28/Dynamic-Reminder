package com.example.projectmodel;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class taskDAO{

    connectionClass connClass;
    Connection conn;
    ResultSet rs;
    String name,str;
    public taskDAO()
    {
        connClass =new connectionClass();
        connect();
    }


    public interface TaskCallback {
        void onTasksLoaded(ArrayList<Task> taskList);
    }

    public void getAllTasks(TaskCallback callback) {
        ArrayList<Task> list = new ArrayList<>();


        new Thread(() -> {
            try (Connection conn = connClass.CONN();
                 PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM Task_Table");
                 ResultSet rs = pstmt.executeQuery()) {

                while (rs.next()) {
                    int id = rs.getInt("id");
                    String name = rs.getString("name");
                    String appx_time = rs.getString("appx_time");
                    String near_activity = rs.getString("near_activity");
                    String date_date = rs.getString("date_date");
                    String time_time = rs.getTime("time_time").toString();

                    list.add(new Task(id, name, appx_time, near_activity, date_date, time_time));
                }

                callback.onTasksLoaded(list); // Notify callback with fetched data

            } catch (SQLException e) {
                e.printStackTrace(); // Log or handle the exception appropriately
            }
        }).start();
    }

    public interface TaskInsertCallback {
        void onTaskInserted(boolean success);
    }

    public void insertTask(Task task, TaskInsertCallback taskInsertCallback) {

        new Thread(() -> {
            try (Connection conn = connClass.CONN()) {
                String query = "INSERT INTO Task_Table (name, appx_time, near_activity, date_date, time_time) VALUES ( ?, ?, ?, ?,?)";
                try (PreparedStatement pstmt = conn.prepareStatement(query)) {
                    System.out.println(task.getTitle()+", "+task.getappx_time()+", "+task.getNear_activity()+", "+task.getDate()+","+task.getTime()+".");
                    pstmt.setString(1,task.getTitle());
                    pstmt.setString(2,task.getappx_time());
                    pstmt.setString(3,task.getNear_activity());
                    pstmt.setString(4, task.getDate());
                    pstmt.setString(5, task.getTime());

                    System.out.println(task.getTitle()+", "+task.getappx_time()+", "+task.getNear_activity()+", "+task.getDate()+","+task.getTime()+".");



                    int rowsAffected = pstmt.executeUpdate();
                    if (rowsAffected > 0) {
                        taskInsertCallback.onTaskInserted(true); // Notify callback on successful insertion

                    } else {
                       taskInsertCallback.onTaskInserted(false); // Notify callback on failed insertion

                    }
                }
            } catch (SQLException e) {
                e.printStackTrace(); // Log or handle the exception appropriately
                taskInsertCallback.onTaskInserted(false); // Notify callback on exception


            }
        }).start();
    }


    public void editTask(Task task, TaskInsertCallback taskInsertCallback) {
          System.out.println("task in editTask"+task );
        new Thread(() -> {
            try (Connection conn = connClass.CONN()) {
                String query = "UPDATE Task_Table SET name = ?, appx_time = ?, near_activity = ?, date_date = ?, time_time = ? WHERE id = ?";
                try (PreparedStatement pstmt = conn.prepareStatement(query)) {
                    pstmt.setString(1, task.getTitle());
                    pstmt.setString(2, task.getappx_time());
                    pstmt.setString(3, task.getNear_activity());
                    pstmt.setString(4, task.getDate());
                    pstmt.setString(5, task.getTime());
                    pstmt.setInt(6, task.getKey()); // Assuming taskId is the ID of the task to be updated

                    System.out.println(task.getKey()+","+task.getTitle()+", "+task.getappx_time()+", "+task.getNear_activity()+", "+task.getDate()+","+task.getTime()+".");

                    int rowsAffected = pstmt.executeUpdate();
                    System.out.println("rowsAffected:"+rowsAffected);
                    if (rowsAffected > 0) {
                        taskInsertCallback.onTaskInserted(true); // Notify callback on successful update
                    } else {
                        taskInsertCallback.onTaskInserted(false); // Notify callback if no rows were affected (task not found)
                    }

                }
            }catch (SQLException e) {
                e.printStackTrace(); // Log or handle the exception appropriately
                taskInsertCallback.onTaskInserted(false); // Notify callback on exception


            }
        }).start();
    }


    public void connect(){
        ExecutorService executorService= Executors.newSingleThreadExecutor();
        executorService.execute(()->{
            try {
                conn = connClass.CONN();
                if (conn == null) {
                    str = "Error in connection";
                } else {
                    str = "Connected with MySQL";
                }
            }  catch(Exception e)
            {
                throw new RuntimeException(e);
            }



        });
    }

    public interface DeleteCallback {
        void onTaskDeleted(boolean isSuccess);
    }
    public void deleteTask(int taskId, DeleteCallback callback) {
        new Thread(() -> {
            try (Connection conn = connClass.CONN();
                 PreparedStatement pstmt = conn.prepareStatement("DELETE FROM Task_Table WHERE id = ?")) {


                pstmt.setInt(1, taskId);

                int rowsAffected = pstmt.executeUpdate();

                if (rowsAffected > 0) {
                    callback.onTaskDeleted(true);
                } else {
                    callback.onTaskDeleted(false);
                }

            } catch (SQLException e) {
                e.printStackTrace();
                callback.onTaskDeleted(false);
            }
        }).start();
    }


}
