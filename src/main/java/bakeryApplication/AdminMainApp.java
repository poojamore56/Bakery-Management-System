package bakeryApplication;

import dao.AdminService;
import dao.Service;
import dto.Product;
import dto.User;

import java.util.List;
import java.util.Scanner;

public class AdminMainApp {
    public static Scanner sc=new Scanner(System.in);
    public static AdminService admin=new AdminService();
    public static Service adminService=new AdminService();
    public static void main(User user){
        System.out.println("this is admin page "+ user.getUserName());
        System.out.println("Select Option");
        System.out.println("1.Add Product");
        System.out.println("2.Display Product");
        System.out.println("3.Display Product By Name");
        System.out.println("4.Update Product");
        System.out.println("5.Delete Product");
        System.out.println("6.Back to Login");
        int ch= sc.nextInt();
        switch (ch){
            case 1:
                addProduct();
                break;
            case 2:
                displayAllProducts();
                break;
            case 3:
                displayProductByName();
                break;
            case 4:
                updateProduct();
                break;
            case 5:
                removeProduct();
            case 6:
                MainApp.main(null);
                break;
            default:
                System.out.println("Invalid option");
        }
        main(user);

    }


    private static void addProduct() {
        System.out.println("Enter Product Name");
        String pName= sc.next();
        System.out.println("Enter Product Price");
        double pPrice= sc.nextDouble();
        System.out.println("Enter Product Qty");
        int pQty= sc.nextInt();

        Product newProduct=new Product(pName,pPrice,pQty);
        int n=admin.addProduct(newProduct);
        System.out.println(n+" Product Added Successfully !!");
        System.out.println("\n\n\n");
    }
    private static void displayAllProducts() {
        List<Product> productList=adminService.displayAllProduct();
        System.out.println("pId   pName   pPrice   pQty");
        System.out.println("================================");
        for (Product p:productList){
            System.out.println(p.getProductId()+" \t "+p.getProductName()+" \t "+p.getProductPrice()+" \t "+p.getProductQty());
            System.out.println("-------------------------------");
        }
        if (productList.isEmpty()){
            System.out.println("No Product Found !!");
        }
        System.out.println("\n\n\n");

    }
    private static void displayProductByName() {
        System.out.println("Enter Product Name");
        String pName= sc.next();
        List<Product>productList=adminService.displayProductByName(pName);
        System.out.println("pId\tpName\tpPrice\tpQty");
        System.out.println("================================");
        for (Product p:productList){
            System.out.println(p.getProductId()+"\t"+p.getProductName()+"\t"+p.getProductPrice()+"\t"+p.getProductQty());
        }
        if (productList.isEmpty()){
            System.out.println("No Product Found !!");
        }
        System.out.println("------------------------------------");
        System.out.println("\n\n\n");
    }

    private static void updateProduct() {
        System.out.println("Enter Product Id For Update");
        int pId= sc.nextInt();
        System.out.println("Enter Product Name ");
        String pName= sc.next();
        System.out.println("Enter Product Price ");
        double pPrice= sc.nextDouble();
        System.out.println("Enter Product Qty ");
        int pQty=sc.nextInt();

        Product updateProduct=new Product(pId,pName,pPrice,pQty);
        int n=admin.updateProduct(updateProduct);
        System.out.println(n+" Product Updated !!");
        System.out.println("\n\n\n");
    }
    private static void removeProduct() {
        System.out.println("Enter Product Id");
        int pId= sc.nextInt();
        int n=admin.removeProduct(pId);
        System.out.println(n+"Product Deleted !!");
        System.out.println("\n\n\n");
    }
}