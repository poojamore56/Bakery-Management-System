package bakeryApplication;

import dao.CustomerService;
import dto.Order;
import dto.Product;
import dto.User;

import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class CustomerMainApp {
    public static Scanner sc=new Scanner(System.in);
    public static CustomerService customer=new CustomerService();
    public static void main(User user){
        System.out.println("this is customer page "+ user.getUserName());
        System.out.println("Select Option ");
        System.out.println("1. DISPLAY ALL PRODUCTS");
        System.out.println("2. PLACE ORDER ");
        System.out.println("3. CANCEL ORDER ");
        System.out.println("4. DISPLAY ALL ORDERS ");
        System.out.println("5. SIGN OUT ");
        int ch = sc.nextInt() ;

        switch (ch){
            case 1:
                displayAllProducts();
                break;
            case 2:
                placeOrder(user);
                break;
            case 3 :
                cancelOrder(user) ;
                break;
            case 4:
                displayAllOrders(user);
                break;
            case 5 :
                user = null ;
                return;
            default:
                System.out.println("INVALID INPUT ");
        }

        main(user);
    }

    private static void displayAllProducts() {
        List<Product> productList = customer.displayAllProduct() ;

        System.out.println("NAME  \t  PRICE ");
        for (Product p : productList)
        {
            System.out.println(p.getProductName() +"\t  "+p.getProductPrice());
        }
        System.out.println("\n\n\n");
    }
    private static void placeOrder(User user) {
        Order ord=new Order(user);
        do {


            System.out.println("Enter Product Name");
            String pName = sc.next();
            System.out.println("Enter Product Qty");
            int pQty = sc.nextInt();
            Product product = new Product(pName, pQty);
            ord.addProduct(product);

            System.out.println("ADD MORE PRODUCT (Y/N)");
            char ch = sc.next().charAt(0);
            if (ch == 'n' || ch == 'N')
                break;
        }while (true);

        boolean status = false;

        status = customer.placeOrder(ord);

        if (status)
            System.out.println("ORDER PLACED !!");
        else
            System.out.println("ORDER NOT PLACED !!");

        System.out.println("\n\n\n");
    }

    private static void cancelOrder(User user) {
        System.out.println("Order ID");
        for (Order o:customer.displayAllOrder(user)){
            System.out.println(o.getOrderId());
        }

        System.out.println("Select Order Id");
        int oId= sc.nextInt();
        System.out.println("PId\t\tProduct Name");
        for (Product p:customer.displayAllProduct(oId)){
            System.out.println(p.getProductId()+"\t\t"+p.getProductName());
        }
        System.out.println("Enter Product Id to Remove ");
        int pid= sc.nextInt();
        Product p=new Product(pid);
        boolean n=customer.cancelOrder(p,oId);
        if (n) {
            System.out.println("Product Removed ");
        }
        System.out.println("\n\n\n");
    }

    private static void displayAllOrders(User user) {
        System.out.println("Order ID");
        for (Order o:customer.displayAllOrder(user)){
            System.out.println(o.getOrderId());
        }

        System.out.println("Select Order Id");
        int oId= sc.nextInt();
        System.out.println("PId\t\tProduct Name");
        for (Product p:customer.displayAllProduct(oId)){
            System.out.println(p.getProductId()+"\t\t"+p.getProductName());
        }
    }


}