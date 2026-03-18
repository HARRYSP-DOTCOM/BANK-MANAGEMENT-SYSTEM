package backend;
import java.util.*;

class Main
{
    public static void main(String args[])
    {
        Scanner scan = new Scanner(System.in);
        banksystem bank = new banksystem();

        while(true)
        {
            System.out.println("\n1 Create Account");
            System.out.println("2 Deposit");
            System.out.println("3 Withdraw");
            System.out.println("4 Check Balance");
            System.out.println("5 Exit");
            System.out.print("Enter choice: ");

            int choice = scan.nextInt();

            // CREATE ACCOUNT
            if(choice == 1)
            {
                System.out.print("Enter acc no: ");
                long acc = scan.nextLong();

                System.out.print("Enter name: ");
                String name = scan.next();
                bank.createAccount(acc, name);
                System.out.println("Account created successfully!");
            }

            // DEPOSIT
            else if(choice == 2)
            {
                System.out.print("Enter acc no: ");
                long acc = scan.nextLong();

                bankaccount a = bank.findAccount(acc);

                if(a != null)
                {
                    System.out.print("Enter amount: ");
                    double amt = scan.nextDouble();

                    a.deposit(amt);
                    System.out.println("Deposit successful!");
                }
                else
                {
                    System.out.println("Account not found!");
                }
            }

            // WITHDRAW
            else if(choice == 3)
            {
                System.out.print("Enter acc no: ");
                long acc = scan.nextLong();

                bankaccount a = bank.findAccount(acc);

                if(a != null)
                {
                    System.out.print("Enter amount: ");
                    double amt = scan.nextDouble();

                    if(a.withdraw(amt))
                        System.out.println("Withdrawal successful!");
                    else
                        System.out.println("Insufficient balance!");
                }
                else
                {
                    System.out.println("Account not found!");
                }
            }

            // CHECK BALANCE
            else if(choice == 4)
            {
                System.out.print("Enter acc no: ");
                long acc = scan.nextLong();

                bankaccount a = bank.findAccount(acc);

                if(a != null)
                {
                    System.out.println("Account Holder: " + a.getName());
                    System.out.printf("Current Balance: ₹ %.2f\n", a.getBalance());
                }
                else
                {
                    System.out.println("Account not found!");
                }
            }

            // EXIT
            else if(choice == 5)
            {
                System.out.println("Thank you for using Bank System!");
                break;
            }

            else
            {
                System.out.println("Invalid choice!");
            }
        }

        scan.close();
    }
}