package com.mlsn.midtrans;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.midtrans.sdk.corekit.core.MidtransSDK;
import com.midtrans.sdk.corekit.core.TransactionRequest;
import com.midtrans.sdk.corekit.core.themes.CustomColorTheme;
import com.midtrans.sdk.corekit.models.BillingAddress;
import com.midtrans.sdk.corekit.models.CustomerDetails;
import com.midtrans.sdk.corekit.models.ItemDetails;
import com.midtrans.sdk.corekit.models.ShippingAddress;
import com.midtrans.sdk.uikit.SdkUIFlowBuilder;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    Button btnSnap;
    int total =20000;
    int quantityProduct = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initialization();
    }

    private void initialization(){
        btnSnap = this.findViewById(R.id.btnSnap);
        btnListener();
    }

    private void btnListener(){
        btnSnap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                midtransTransaction();
            }
        });
    }

    private void midtransInitialize(){
        SdkUIFlowBuilder.init().setClientKey(Credentials.CLIENT_KEY_MIDTRANS).setContext(getApplicationContext())
                .setMerchantBaseUrl(Credentials.BASE_URL_MIDTRANS)
                .enableLog(true)
                .setColorTheme(new CustomColorTheme("#FFE51255", "#B61548", "#FFE51255"))
                .setLanguage("id")
                .buildSDK();
    }

    private void midtransTransaction(){
        midtransInitialize();
        Toast.makeText(this, "Open transaction", Toast.LENGTH_LONG).show();

        TransactionRequest transactionRequest = new TransactionRequest(
                "Payment-Midtrans" + System.currentTimeMillis(),
                Double.valueOf(total)
        );

        ItemDetails detailItem = new ItemDetails(
                "id Product",
                Double.valueOf(total),
                quantityProduct,
                "Product Description"
        );

        ArrayList<ItemDetails> itemDetails = new ArrayList<>();

        itemDetails.add(detailItem);
        uiKitDetails(transactionRequest);
        transactionRequest.setItemDetails(itemDetails);
        MidtransSDK.getInstance().setTransactionRequest(transactionRequest);
        MidtransSDK.getInstance().startPaymentUiFlow(this);
    }

    private void uiKitDetails(TransactionRequest transactionRequest){
        CustomerDetails customerDetails = new CustomerDetails();
        customerDetails.setCustomerIdentifier("Testing");
        customerDetails.setPhone("085743216700");
        customerDetails.setFirstName("User");
        customerDetails.setLastName("Testing");
        customerDetails.setEmail("Tester@mail.com");

        ShippingAddress shippingAddress = new ShippingAddress();
        shippingAddress.setAddress("Jalan MT Haryono");
        shippingAddress.setCity("Bantul");
        shippingAddress.setPostalCode("55711");
        customerDetails.setShippingAddress(shippingAddress);

        BillingAddress billingAddress = new BillingAddress();
        billingAddress.setAddress("Jalan MT Haryono");
        billingAddress.setCity("Bantul");
        billingAddress.setPostalCode("55711");
        customerDetails.setBillingAddress(billingAddress);

        transactionRequest.setCustomerDetails(customerDetails);
    }

}