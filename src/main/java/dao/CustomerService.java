package dao;

import dto.Order;
import dto.Product;
import dto.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CustomerService extends ServiceImpl{
    @Override
    public List<Product> displayAllProduct() {
        String selectQuery = "SELECT product_name , product_price FROM product_info ";
        List<Product> productList = new ArrayList<>();
        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(selectQuery);
            while (rs.next()) {
                String name = rs.getString(1);
                double price = rs.getDouble(2);
                Product pro = new Product(name , price);
                productList.add(pro);
            }
        } catch (SQLException e) {
            System.out.println(e);
        }
        return productList;
    }

    @Override
    public List<Product> displayProductByName(String productName) {
        return null;
    }

    public boolean placeOrder(Order ord) {
        boolean status=false;
        String insertUserProcedure="{call insertUser(?,?,?)}";
        String placeOrderProcedure="{call placeOrder(?,?,?)}";
        int ordID=0;
        try {
            CallableStatement cstmt = conn.prepareCall(insertUserProcedure);
            for (Product p1:ord.getProductList()) {
                cstmt.setInt(1, ord.getUser().getUserId());
                cstmt.setString(2, p1.getProductName());
                cstmt.setInt(3,p1.getProductQty());

            }
            cstmt.execute();
            ResultSet rs=cstmt.getResultSet();
            if (rs==null){
                System.out.println("Out Of Stock");
                return false;
            }
            while (rs.next()){
                ordID=rs.getInt(1);
                status=true;
            }

            cstmt=conn.prepareCall(placeOrderProcedure);
            for ( Product p:ord.getProductList()){
                cstmt.setInt(1,ordID);
                cstmt.setString(2,p.getProductName());
                cstmt.setInt(3,p.getProductQty());
                cstmt.execute();
            }
            String displayQuery="SELECT \n" +
                    "    u.user_name, p.product_name, op.product_qty,p.product_price*op.product_qty as total\n" +
                    "FROM\n" +
                    "    user_info u\n" +
                    "        INNER JOIN\n" +
                    "    order_info o\n" +
                    "        INNER JOIN\n" +
                    "    order_product op\n" +
                    "        INNER JOIN\n" +
                    "    product_info p \n" +
                    "    \n" +
                    "ON \n" +
                    "\t\tu.user_id = o.user_id\n" +
                    "        AND o.order_id = op.order_id\n" +
                    "        AND op.product_id = p.product_id\n" +
                    "        where o.order_id=?;";
            PreparedStatement pstmt=conn.prepareStatement(displayQuery);
            pstmt.setInt(1,ordID);
            ResultSet rs2=pstmt.executeQuery();
            double totalAmt=0;
            while (rs2.next()){
                totalAmt=totalAmt+rs2.getInt("total");
            }

            String updateTotal="update order_info set order_amt=? where order_id=?";
            PreparedStatement pstmt2=conn.prepareStatement(updateTotal);
            pstmt2.setDouble(1,totalAmt);
            pstmt2.setInt(2,ordID);
            System.out.println("Total Amount Is "+totalAmt);
            pstmt2.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e);
        }
        return status;
    }

    public List<Order> displayAllOrder(User user) {
        List<Order> orderList =new ArrayList<>();
        String selectQuery="Select order_id from order_info where user_id="+user.getUserId();

        try {
            Statement stmt= conn.createStatement();
            ResultSet rs=stmt.executeQuery(selectQuery);
            while (rs.next()){
                int oId=rs.getInt(1);
                Order o1=new Order();
                o1.setOrderId(oId);
                orderList.add(o1);
            }
        } catch (SQLException e) {
            System.out.println(e);
        }

        return orderList;
    }

    public List<Product> displayAllProduct(int oId) {
        List<Product> productList=new ArrayList<>();
        String selectQuery="select" +
                " p.product_id ,p.product_name " +
                "from product_info p inner join order_product op " +
                " on "+
                " p.product_id=op.product_id "+
                " where order_id=?";
        try {
            PreparedStatement pstmt= conn.prepareStatement(selectQuery);
            pstmt.setInt(1,oId);
            ResultSet rs= pstmt.executeQuery();
            while (rs.next()){
                Product p=new Product(rs.getInt(1),rs.getString(2));
                productList.add(p);

            }
        } catch (SQLException e) {
            System.out.println(e);
        }
        return productList;
    }

    public boolean cancelOrder(Product p, int oId) {
        String deleteQuery="{ call cancelOrder(?,?,?)}";
        boolean status=false;
        try {
            CallableStatement cstmt= conn.prepareCall(deleteQuery);
            cstmt.setInt(1,p.getProductId());
            cstmt.setInt(2,oId);
            cstmt.execute();
            status=cstmt.getBoolean(3);
            if (!status){
                System.out.println("Not Removed !!");
            }
            if (status){
                String totalQuery="select order_amt from order_info where order_id="+oId;
                Statement stmt=conn.createStatement();
                ResultSet rs=stmt.executeQuery(totalQuery);
                while (rs.next()){
                    System.out.println("Now Total Amount Is "+rs.getDouble(1));
                }
            }
        } catch (SQLException e) {
            System.out.println(e);
        }
        return status;
    }
}