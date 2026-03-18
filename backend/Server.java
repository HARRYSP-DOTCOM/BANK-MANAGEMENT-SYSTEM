package backend;
import com.sun.net.httpserver.*;
import java.io.*;
import java.net.InetSocketAddress;
import java.util.*;

class Account {
    long accNo;
    String name;
    double balance;

    Account(long accNo, String name) {
        this.accNo = accNo;
        this.name = name;
        this.balance = 0;
    }
}

public class Server {

    static Map<Long, Account> accounts = new HashMap<>();

    public static void main(String[] args) throws Exception {

        HttpServer server = HttpServer.create(new InetSocketAddress("0.0.0.0", 8081), 0);

        //  SERVE index.html — MUST send a response for ALL paths, not just "/"
        server.createContext("/", exchange -> {
            String path = exchange.getRequestURI().getPath();

            if (path.equals("/") || path.equals("/index.html")) {
                File file = new File("frontend/index.html");
                if (file.exists()) {
                    byte[] bytes = java.nio.file.Files.readAllBytes(file.toPath());
                    exchange.getResponseHeaders().set("Content-Type", "text/html; charset=UTF-8");
                    exchange.getResponseHeaders().set("Access-Control-Allow-Origin", "*");
                    exchange.sendResponseHeaders(200, bytes.length);
                    exchange.getResponseBody().write(bytes);
                    exchange.getResponseBody().close();
                } else {
                    String err = "index.html not found!";
                    byte[] errBytes = err.getBytes();
                    exchange.sendResponseHeaders(404, errBytes.length);
                    exchange.getResponseBody().write(errBytes);
                    exchange.getResponseBody().close();
                }
            } else {
                //  CRITICAL: Send 404 for any unknown path
                // Without this, requests to /withdraw, /deposit etc.
                // that fall through here will hang with no response
                String err = "Not found: " + path;
                byte[] errBytes = err.getBytes();
                exchange.sendResponseHeaders(404, errBytes.length);
                exchange.getResponseBody().write(errBytes);
                exchange.getResponseBody().close();
            }
        });

        //  CREATE ACCOUNT
        server.createContext("/create", exchange -> {
            if (handleOptions(exchange)) return;

            String body = new String(exchange.getRequestBody().readAllBytes());
            Scanner sc = new Scanner(body);
            long acc = sc.nextLong();
            String name = sc.next();
            sc.close();

            if (accounts.containsKey(acc)) {
                sendResponse(exchange, "Account already exists!");
            } else {
                accounts.put(acc, new Account(acc, name));
                sendResponse(exchange, "Account created successfully for " + name + "!");
            }
        });

        //  DEPOSIT
        server.createContext("/deposit", exchange -> {
            if (handleOptions(exchange)) return;

            String body = new String(exchange.getRequestBody().readAllBytes());
            Scanner sc = new Scanner(body);
            long acc = sc.nextLong();
            double amt = sc.nextDouble();
            sc.close();

            Account a = accounts.get(acc);
            if (a != null) {
                a.balance += amt;
                sendResponse(exchange, "Deposit successful! New balance: " + a.balance);
            } else {
                sendResponse(exchange, "Account not found!");
            }
        });

        //  WITHDRAW
        server.createContext("/withdraw", exchange -> {
            if (handleOptions(exchange)) return;

            String body = new String(exchange.getRequestBody().readAllBytes());
            Scanner sc = new Scanner(body);
            long acc = sc.nextLong();
            double amt = sc.nextDouble();
            sc.close();

            Account a = accounts.get(acc);
            if (a != null) {
                if (a.balance >= amt) {
                    a.balance -= amt;
                    sendResponse(exchange, "Withdraw successful! New balance: " + a.balance);
                } else {
                    sendResponse(exchange, "Insufficient balance! Current balance: " + a.balance);
                }
            } else {
                sendResponse(exchange, "Account not found!");
            }
        });

        //  BALANCE
        server.createContext("/balance", exchange -> {
            if (handleOptions(exchange)) return;

            String body = new String(exchange.getRequestBody().readAllBytes());
            long acc = Long.parseLong(body.trim());

            Account a = accounts.get(acc);
            if (a != null) {
                sendResponse(exchange, "Account: " + a.name + " | Balance: " + a.balance);
            } else {
                sendResponse(exchange, "Account not found!");
            }
        });

        server.setExecutor(null);
        server.start();

        System.out.println("✅ Server running at: http://localhost:8081");
    }

    static boolean handleOptions(HttpExchange exchange) throws IOException {
        if (exchange.getRequestMethod().equalsIgnoreCase("OPTIONS")) {
            exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
            exchange.getResponseHeaders().add("Access-Control-Allow-Methods", "GET, POST, OPTIONS");
            exchange.getResponseHeaders().add("Access-Control-Allow-Headers", "Content-Type");
            exchange.sendResponseHeaders(204, -1);
            return true;
        }
        return false;
    }

    static void sendResponse(HttpExchange exchange, String response) throws IOException {
        byte[] res = response.getBytes("UTF-8");
        exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
        exchange.getResponseHeaders().add("Access-Control-Allow-Methods", "GET, POST, OPTIONS");
        exchange.getResponseHeaders().add("Access-Control-Allow-Headers", "Content-Type");
        exchange.getResponseHeaders().set("Content-Type", "text/plain; charset=UTF-8");
        exchange.sendResponseHeaders(200, res.length);
        exchange.getResponseBody().write(res);
        exchange.getResponseBody().close();
    }
}
