package com.company;


import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

import java.sql.*;




public class Main {
    static Connection c = null;
    static Statement stmt = null;

    public static void connect(String a){


        try {
            Class.forName("org.sqlite.JDBC");
            //c = DriverManager.getConnection("jdbc:sqlite:test.db");
            c = DriverManager.getConnection("jdbc:sqlite:"+a);

            System.out.println("Opened database successfully");

            stmt = c.createStatement();
            String sql = "CREATE TABLE card " +
                    "(id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    " number TEXT, " +
                    " pin TEXT, " +
                    " balance INTEGER DEFAULT 0)";
            stmt.executeUpdate(sql);
            // stmt.close();
            // c.close();


        } catch ( Exception e ) {
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
            System.exit(0);
        }
        //System.out.println("Opened database successfully");
    }

    public static void insert(String card, String pin) {
        try {
            stmt = c.createStatement();
            String sql = "INSERT INTO card (number ,pin) "+
                    "VALUES ("+card+","+pin+");";
            stmt.executeUpdate(sql);



        } catch (Exception e){
            System.out.println(e.getMessage()+" error");
        }
    }



    static public long luhn(long a){



        // System.out.println(a);
        int sum=0;
        String card = Long.toString(a);
        for(int i=0;i<16;i+=2){
            int digit = Integer.parseInt(String.valueOf(card.charAt(i)));
            //System.out.print(digit+" ");
            digit *=2;
            if(digit>9){
                digit -=9;
            }
            //System.out.println(digit);
            sum +=digit;
        }
        for (int i = 1; i <16 ; i+=2) {
            int digit = Integer.parseInt(String.valueOf(card.charAt(i)));
            sum += digit;
        }
        //System.out.println(sum);


        if(a%10+(10-sum%10)<=10){
            if(a%10+(10-sum%10)==10) {
                return a-a%10;
            }
            else {
                return a + (10 - sum % 10);
            }
        } else{
            return a-sum%10;

        }
    }


    static boolean luhn_check(long a){



        // System.out.println(a);
        int sum=0;
        String card = Long.toString(a);
        for(int i=0;i<16;i+=2){
            int digit = Integer.parseInt(String.valueOf(card.charAt(i)));
            //System.out.print(digit+" ");
            digit *=2;
            if(digit>9){
                digit -=9;
            }
            //System.out.println(digit);
            sum +=digit;
        }
        for (int i = 1; i <16 ; i+=2) {
            int digit = Integer.parseInt(String.valueOf(card.charAt(i)));
            sum += digit;
        }
        //System.out.println(sum);


        if(sum%10==0){
            return true;
        } else return false;
    }

    public static void main(String[] args) {

        String ww = args[1];
        File file = new File (ww);
        if(!file.exists()) {
            System.out.println(1);
            connect(args[1]);

        } else {
            try {
                Class.forName("org.sqlite.JDBC");
                c = DriverManager.getConnection("jdbc:sqlite:" + args[1]);
            } catch (Exception e){
                System.out.println(e.getMessage());
            }
        }
        Scanner scanner = new Scanner(System.in);
        List<Long> cards = new ArrayList<Long>();
        List<Integer> pins = new ArrayList<>();
        int move =-1;
        while(move !=0){
            System.out.println("1. Create an account");
            System.out.println("2. Log into account");
            System.out.println("0. Exit");
            move = scanner.nextInt();
            if(move==1){
                Random random = new Random();
                long card_tmp=Long.parseUnsignedLong("4000000000000000");
                long a1= (long)random.nextInt(10);
                long a2= (long)random.nextInt(10)*10;
                long a3= (long)random.nextInt(10)*100;
                long a4= (long)random.nextInt(10)*1000;
                long a5= (long)random.nextInt(10)*10000;
                long a6= (long)random.nextInt(10)*100000;
                long a7= (long)random.nextInt(10)*1000000;
                long a8= (long)random.nextInt(10)*10000000;
                long a9= (long)random.nextInt(10)*100000000;
                long a10= (long)random.nextInt(10)*100000000;
                card_tmp +=a1+a2+a3+a4+a5+a6+a7+a8+a9+a10;


                // luhn
                card_tmp=luhn(card_tmp);

                int card_pin = random.nextInt(10000-1000+1)+1000;
                insert( Long.toString(card_tmp), Integer.toString(card_pin));
                //cards.add(card_tmp);

                // pins.add(card_pin);
                System.out.println("Your card has been created");
                System.out.println("Your card number:");
                System.out.println(card_tmp);
                System.out.println("Your card PIN:");
                System.out.println(card_pin);
            }
            if(move==2){

                System.out.println("Enter your card number:");
                long tmp_card = scanner.nextLong();
                System.out.println("Enter your PIN:");
                int tmp_pin = scanner.nextInt();
                ResultSet rs1;
                ResultSet rs2;
                try {
                    stmt = c.createStatement();
                    rs1  = stmt.executeQuery("SELECT * FROM card WHERE number="+tmp_card+" AND pin="+tmp_pin+";");
                    stmt = c.createStatement();
                    // rs2  = stmt.executeQuery("SELECT * FROM card WHERE pin="+tmp_pin+";");
                    // System.out.println("select");
                    if(rs1.next() ){
                        //System.out.println("enter");
                        //rs1.next();
                        // rs2.next();
                        System.out.println("You have successfully logged in!");
                        int success_in = -1;
                        while(success_in!=0 && success_in!=4 && success_in!=5){
                            System.out.println("1. Balance");
                            System.out.println("2. Add income");
                            System.out.println("3. Do transfer");
                            System.out.println("4. Close account");
                            System.out.println("5. Log out");
                            System.out.println("0. Exit");
                            success_in = scanner.nextInt();
                            if(success_in==1){
                                stmt = c.createStatement();
                                rs1  = stmt.executeQuery("SELECT balance FROM card WHERE number="+tmp_card+";");
                                System.out.println("Balance: "+rs1.getInt("balance"));
                            }
                            if(success_in==2) {
                                System.out.println("Enter income:");
                                int tmp_inc = scanner.nextInt();
                                stmt = c.createStatement();
                                stmt.executeUpdate("UPDATE card SET balance = balance +"+tmp_inc+" WHERE number="+tmp_card+";");
                            }
                            if(success_in==3) {
                                System.out.println("Transfer");
                                System.out.println("Enter card number:");
                                long tmp_card_check = scanner.nextLong();
                                if(tmp_card!=tmp_card_check) {
                                    if (luhn_check(tmp_card_check)) {
                                        stmt = c.createStatement();
                                        rs1 = stmt.executeQuery("SELECT * FROM card WHERE number=" + tmp_card_check + ";");
                                        if (rs1.next()) {
                                            System.out.println("Enter how much money you want to transfer:");
                                            int sum_trans = scanner.nextInt();
                                            Statement stmt2 = null;
                                            stmt2 = c.createStatement();
                                            ResultSet rs3;
                                            rs3 = stmt.executeQuery("SELECT balance FROM card WHERE number=" + tmp_card + ";");
                                            if (rs3.getInt("balance") >= sum_trans) {
                                                stmt2 = c.createStatement();
                                                stmt2.executeUpdate("UPDATE card SET balance = balance +" + sum_trans + " WHERE number=" + tmp_card_check + ";");

                                                stmt2 = c.createStatement();
                                                stmt2.executeUpdate("UPDATE card SET balance = balance -" + sum_trans + " WHERE number=" + tmp_card + ";");
                                                System.out.println("Success!");

                                            } else {
                                                System.out.println("Not enough money!");
                                            }

                                        } else {
                                            System.out.println("Such a card does not exist.");
                                        }

                                    } else {
                                        System.out.println("Probably you made mistake in the card number. Please try again!");
                                    }
                                } else {
                                    System.out.println("You can't transfer money to the same account!");
                                }


                            }
                            if(success_in==4){
                                stmt = c.createStatement();
                                stmt.executeUpdate("DELETE FROM card WHERE number="+tmp_card+";");
                            }
                            if(success_in==0){
                                move =0;
                            }

                        }
                    } else {
                        System.out.println("Wrong card number or PIN!");
                    }


                } catch (Exception e){
                    System.out.println(e.getMessage()+" error select");
                }
            }
        }
        try {
            c.close();
        }catch (Exception e){

        }
    }
}
