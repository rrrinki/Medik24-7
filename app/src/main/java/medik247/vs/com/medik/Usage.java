package medik247.vs.com.medik;

/**
 * Created by VS3 on 6/5/2017.
 */
import base.ResponseParser;
        import base.exceptions.AuthenticationException;
        import base.exceptions.InvalidInputException;
        import base.exceptions.NoDataException;
        import base.exceptions.NoMessageException;
        import features.Client;
        import features.Invoice;
        import features.Miscellaneous;
        import features.Payment;
        import features.Product;
        import features.Transfer;
        import features.objects.ClientObject;
        import features.objects.InvoiceObject;
        import features.objects.PaymentObject;
        import features.objects.ProductObject;
        import features.objects.TransferObject;
        import java.io.IOException;
        import java.util.ArrayList;
        import java.util.List;
        import java.util.Map;
        import org.json.JSONException;
import org.json.JSONObject;

import utils.AccountDetails;
        import utils.Item;
        import utils.constants.ClientType;
        import utils.constants.FeeBearer;
        import utils.constants.HistoryPeriod;
        import utils.constants.Implementation;
        import utils.constants.PaymentChannel;
        import utils.constants.ProductType;

/**
 *
 * @author Yusuf Oguntola
 */
public class Usage {

    //This is a full sample of how to use the client features
    void usingClientFeatures() {
        try {
            //Initialize client. Use Implementation.LIVE when going Live
            Client client = new Client("6f8ee4c4a058bd588f47a276fa2c336e040924b9", Implementation.DEMO);

            String jsonStr = "{\n" +
                    "      \"key\": \"6f8ee4c4a058bd588f47a276fa2c336e040924b9\",\n" +
                    "      \"client\": {\n" +
                    "            \"first_name\": \"Albert\",\n" +
                    "            \"last_name\": \"Jane\",\n" +
                    "            \"email\": \"jane@alberthospital.com\",\n" +
                    "            \"phone\": \"+2348012345678\"\n" +
                    "        },\n" +
                    "      \"due_date\": \"12/30/2016\",\n" +
                    "      \"fee_bearer\": \"client\",\n" +
                    "      \"items\": [\n" +
                    "        {\n" +
                    "          \"item\": \".Com Domain Name Registration\",\n" +
                    "          \"description\": \"alberthostpital.com\",\n" +
                    "          \"unit_cost\": \"2500.00\",\n" +
                    "          \"quantity\": \"1\"\n" +
                    "        }" ;
            JSONObject jsonObject =new JSONObject(jsonStr);
            //Add a new Client using very few information
            ClientObject newClient = new ClientObject(jsonObject);//client.addClient("First Name", "Last Name", "Email", "Phone");
            //OR add all the details using an empty string for information that's not available
            newClient = client.addClient("First Name", "Last Name", "Email", "Phone", "Bank Id", "Account Number", "Website", "Address", ClientType.CUSTOMER, "Company Name");
            //Use the ClientObject object to get details of the newly registered client.
            String clientId = newClient.getClientId();
            String accountNumber = newClient.getAccountNumber();

            // Retrieve a client
            ClientObject oldClient = client.getClient(clientId);

            //Edit a client. The two variants of addClient also exist for editClient
            ClientObject editedClient = client.editClient(clientId, "First Name", "Last Name", "Email", "Phone");

            //Delete Client
            ResponseParser parser = client.deleteClient(clientId);

            //You can view the details of the request by checking the parser object. e.g:
            String message = parser.getMessage();
            String status = parser.getStatus();
            String data = parser.getData(); //This would throw a NoDataException if there's no data in the response.

            //A client object also has a parser in it to view the response details.
            //You can obtain it by doing:
            ResponseParser parser1 = newClient.getParser();

        } catch (AuthenticationException | InvalidInputException | IOException | JSONException | NoMessageException | NoDataException ex) {

        }
    }

    //This is a full example of how to use the invoice features
    private void usingInvoiceFeatures() {

        try {
            //Initialize the invoice class
            Invoice invoice = new Invoice("YOUR_PRIVATE_KEY", Implementation.DEMO);

            //Add a new invoice
            /*
            For an invoice, you'd need a client to add the invoice for.
            Either create a new client or add a new client to get a clientObject to use.
            e.g ClientObject client Object = client.getClient("client id");
            You'd also need to create a list of items to add to the invoice.
            The list may however contain just one item.
             */
            ClientObject client = new Client("YOUR_PRIVATE_KEY", Implementation.DEMO).getClient("47");
            List<Item> items = new ArrayList<>();
            items.add(new Item("Another", "Another Item", "20000", 1));
            items.add(new Item("Second", "Another Second Item", "25000", 5));
            items.add(new Item("Third", "Another Third Item", "10000", 3));
            InvoiceObject newInvoice = invoice.addInvoice(client, "12/30/2016", FeeBearer.ACCOUNT, (Item) items);

            //Retrieve an invoice
            // Reference code may be obtained from an invoice object.
            InvoiceObject oldInvoice = invoice.getInvoice("Reference Code");

            //Delete an invoice
            ResponseParser parser = invoice.deleteInvoice("Reference Code");

            //Send an invoice
            InvoiceObject sentInvoice = invoice.sendInvoice("Reference Code");

            //Invoice History
            /*
            //Retrieving between a preset history period.
            Make use of the several period options in HistoryPeriod class.
            e.g: HistoryPeriod.TODAY, HistoryPeriod.ONE_WEEK etc.
             */
            List<InvoiceObject> invoices = invoice.getInvoiceHistory(HistoryPeriod.LAST_NINETY_DAYS);

            //Or use a custom date range
            invoices = invoice.getInvoiceHistory("05/12/2016", "05/12/2017");

            //Get details of an invoice from an invoice object. e.g:
            ClientObject receivingClient = sentInvoice.getClient();
            List<Item> attachedItems = sentInvoice.getItems();
            String dueDate = sentInvoice.getDueDate();
            String feeBearer = sentInvoice.getFeeBearer();
        } catch (AuthenticationException | InvalidInputException | NoDataException | IOException | JSONException e) {

        }
    }

    private void Miscellaneous() {
        try {
            Miscellaneous miscellaneous = new Miscellaneous("YOUR_PRIVATE_KEY", Implementation.DEMO);

            //Retrieve a payment
            PaymentObject payment = miscellaneous.getPayment("Reference Code");
            //The payment object has all the details of the payment in it

            //Retrieve all banks
            //This would return a map of bank codes to bank names
            Map<String, String> banks = miscellaneous.getBanks();

            //Resolve an account
            AccountDetails accountDetails = miscellaneous.resolveAccount("Bank Id", "Account Number");
            String accountName = accountDetails.getAccountName();
            String accountNumber = accountDetails.getAccountNumber();
        } catch (AuthenticationException | InvalidInputException | IOException | JSONException e) {
        }
    }

    private void PaymentExample() {
        try {
            //Initialize payment
            Payment payment = new Payment("YOUR_PRIVATE_KEY", Implementation.DEMO);

            //Add a new payment
            //Get payment channels from PaymentChannel class.
            PaymentObject addedPayment = payment.addPayment("Reference Code", "05/12/2018", "20000", PaymentChannel.CASH);

            //Get a previous payment with it's Reference Code
            PaymentObject payment1 = payment.getPayment("Reference Code");

            //Get payments within a period
            List<PaymentObject> paymentHistory = payment.getPaymentHistory(HistoryPeriod.TODAY);

            //Or within a specified custom date
            List<PaymentObject> paymentHistory1 = payment.getPaymentHistory("05/12/2016", "05/12/2018");

            //Get payment details from PaymentObject. e.g:
            String amount = addedPayment.getAmount();
            ClientObject client = addedPayment.getClient();
        } catch (AuthenticationException | InvalidInputException | IOException | JSONException e) {
        }
    }

    private void ProductExample() {
        try {
            //Initialize product
            Product product = new Product("YOUR_PRIVATE_KEY", Implementation.DEMO);

            //Add a new product
            //Get ProductType from ProductType class
            ProductObject addedProduct = product.addProduct("Name", "Description", "20000", ProductType.PRODUCT);

            //Get a previously added product
            ProductObject product1 = product.getProduct("Product Id");

            //Get all products
            List<ProductObject> products = product.getProducts();

            //Delete a product
            ResponseParser deleteProduct = product.deleteProduct("Product Id");

            //Get product information from ProductObject. e.g:
            String description = addedProduct.getDescription();
        } catch (AuthenticationException | InvalidInputException | IOException | JSONException e) {
        }
    }

    private void TransferExample() {
        try {
            //Initialize transfer
            Transfer transfer = new Transfer("YOUR_PRIVATE_KEY", Implementation.DEMO);

            //Make a transfer from your account to another account.
            TransferObject addedTransfer = transfer.addTransfer("First Name", "Last Name", "Email", "Phone", "Bank code", "Account Number", "Amount");

            //Delete a transfer
            ResponseParser deletedTransfer = transfer.deleteTransfer("Reference code");

            //Get a transfer
            TransferObject transfer1 = transfer.getTransfer("Reference Code");

            //Get transfers made within a period range making use of HistoryPeriod to obtain available periods.
            List<TransferObject> transferHistory = transfer.getTransferHistory(HistoryPeriod.ONE_WEEK);

            //Or get transfer history within a date range
            List<TransferObject> transferHistory1 = transfer.getTransferHistory("05/12/2016", "05/12/2018");

            //Get details of transfer using the TransferObject e.g:
            String accountNumber = addedTransfer.getAccountNumber();
            ClientObject client = addedTransfer.getClient();
            String clientId = addedTransfer.getClientId();
            String accountName = addedTransfer.getAccountName();
        } catch (AuthenticationException | InvalidInputException | IOException | JSONException e) {
        }
    }
}
