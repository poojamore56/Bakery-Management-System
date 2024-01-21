package dao;

import dto.Product;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class AdminService extends ServiceImpl{
    @Override
    public List<Product> displayAllProduct() {
        String displayQuery="Select * from product_info";
        List<Product> productList=new ArrayList<>();
        try {
            Statement stmt= conn.createStatement();
            ResultSet rs=stmt.executeQuery(displayQuery);
            while (rs.next()){
                int pId=rs.getInt(1);
                String pName=rs.getString(2);
                double pPrice=rs.getDouble(3);
                int pQty=rs.getInt(4);
                Product displayProduct=new Product(pId,pName,pPrice,pQty);
                productList.add(displayProduct);
            }
        } catch (SQLException e) {
            System.out.println(e);
        }
        return productList;
    }

    @Override
    public List<Product> displayProductByName(String productName) {
        String query="select * from product_info where product_name=?";
        List<Product> productList=new ArrayList<>();
        try {
            PreparedStatement pstmt=conn.prepareStatement(query);
            pstmt.setString(1,productName);
            ResultSet rs=pstmt.executeQuery();
            while (rs.next()){
                Product product=new Product(rs.getInt(1),rs.getString(2),rs.getDouble(3),rs.getInt(4));
                productList.add(product);
            }
        } catch (SQLException e) {
            System.out.println(e);
        }
        return productList;
    }

    public int addProduct(Product newProduct) {
        int n=0;
        String insertQuery="insert into product_info (product_name,product_price,product_qty) values(?,?,?)";
        try {
            PreparedStatement pstmt= conn.prepareStatement(insertQuery);
            pstmt.setString(1,newProduct.getProductName());
            pstmt.setDouble(2,newProduct.getProductPrice());
            pstmt.setInt(3,newProduct.getProductQty());
            n=pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e);
        }
        return n;
    }

    public int updateProduct(Product updateProduct) {
        int n=0;
        String updateQuery="update product_info set product_name=?,product_price=?,product_qty=? where product_id=?";
        try {
            PreparedStatement pstmt= conn.prepareStatement(updateQuery);
            pstmt.setString(1,updateProduct.getProductName());
            pstmt.setDouble(2,updateProduct.getProductPrice());
            pstmt.setInt(3,updateProduct.getProductQty());
            pstmt.setInt(4,updateProduct.getProductId());
            n=pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e);
        }
        return n;
    }

    public int removeProduct(int pId) {
        int n=0;
        String removeQuery="delete from product_info where product_id=?";
        try {
            PreparedStatement pstmt= conn.prepareStatement(removeQuery);
            pstmt.setInt(1,pId);
            n=pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e);
        }
        return n;
    }
}