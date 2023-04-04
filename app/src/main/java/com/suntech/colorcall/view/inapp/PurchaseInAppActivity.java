package com.suntech.colorcall.view.inapp;


import static com.android.billingclient.api.BillingClient.ProductType.INAPP;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.billingclient.api.AcknowledgePurchaseParams;
import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.BillingClientStateListener;
import com.android.billingclient.api.BillingFlowParams;
import com.android.billingclient.api.BillingResult;
import com.android.billingclient.api.ConsumeParams;
import com.android.billingclient.api.ConsumeResponseListener;
import com.android.billingclient.api.ProductDetails;
import com.android.billingclient.api.Purchase;
import com.android.billingclient.api.QueryProductDetailsParams;
import com.android.billingclient.api.QueryPurchasesParams;
import com.google.common.collect.ImmutableList;
import com.suntech.colorcall.R;
import com.suntech.colorcall.view.MainApp;

import java.util.ArrayList;
import java.util.List;

public class PurchaseInAppActivity extends AppCompatActivity implements PurchaseInAppAdapter.OnClickListener {


    private PurchaseInAppAdapter adapter;
    private BillingClient billingClient;
    private Handler handler;
    private List<ProductDetails> productDetailsList;
    private OnPurchaseResponse onPurchaseResponse;

    private ImageView imvBack;
    private RecyclerView listData;

    private TextView textGold;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchase_in_app);
        imvBack = findViewById(R.id.imvBack);
        listData = findViewById(R.id.listData);
        textGold  = findViewById(R.id.txtGold);
        initViews();

        imvBack.setOnClickListener(v -> {
            onBackPressed();
        });
    }


    private void initViews() {
        textGold.setText(MainApp.getInstance().getPreference().getValueCoin()+"");
        adapter = new PurchaseInAppAdapter();
        listData.setHasFixedSize(true);
        listData.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        listData.setAdapter(adapter);
        adapter.setOnClickListener(this);
        productDetailsList = new ArrayList<>();
        handler = new Handler();
        billingClient = BillingClient.newBuilder(this)
                .enablePendingPurchases()
                .setListener(
                        (billingResult, list) -> {
                            if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK && list != null) {
                                for (Purchase purchase : list) {
                                    verifyInAppPurchase(purchase);
                                }
                            }
                        }
                ).build();
        establishConnection();
    }

    void establishConnection() {
        billingClient.startConnection(new BillingClientStateListener() {
            @Override
            public void onBillingSetupFinished(@NonNull BillingResult billingResult) {
                if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {
                    showProducts();
                }
            }

            @Override
            public void onBillingServiceDisconnected() {
                establishConnection();
            }
        });
    }

    @SuppressLint("SetTextI18n")
    void showProducts() {
        QueryProductDetailsParams params = QueryProductDetailsParams.newBuilder()
                .setProductList(getInAppProductList())
                .build();
        billingClient.queryProductDetailsAsync(
                params,
                (billingResult, prodDetailsList) -> {
                    // Process the result
                    productDetailsList.clear();
                    handler.postDelayed(() -> {
                        productDetailsList.addAll(prodDetailsList);
                        adapter.setData(this, productDetailsList);
                        if (prodDetailsList.size() == 0) {
                            Toast.makeText(PurchaseInAppActivity.this, "prodDetailsList, size = 0", Toast.LENGTH_SHORT).show();
                            onBackPressed();
                        }
                    }, 2000);
                }
        );
    }

    private ImmutableList<QueryProductDetailsParams.Product> getInAppProductList() {
        ImmutableList<QueryProductDetailsParams.Product> productList = ImmutableList.of(
                //Product 1
                QueryProductDetailsParams.Product.newBuilder()
                        .setProductId(Constants.KEY_1_COIN)
                        .setProductType(INAPP)
                        .build(),

                //Product 2
                QueryProductDetailsParams.Product.newBuilder()
                        .setProductId(Constants.KEY_2_COIN)
                        .setProductType(INAPP)
                        .build(),

                //Product 3
                QueryProductDetailsParams.Product.newBuilder()
                        .setProductId(Constants.KEY_3_COIN)
                        .setProductType(INAPP)
                        .build(),

                //Product 4
                QueryProductDetailsParams.Product.newBuilder()
                        .setProductId(Constants.KEY_4_COIN)
                        .setProductType(INAPP)
                        .build(),

                //Product 5
                QueryProductDetailsParams.Product.newBuilder()
                        .setProductId(Constants.KEY_5_COIN)
                        .setProductType(INAPP)
                        .build(),

                //Product 6
                QueryProductDetailsParams.Product.newBuilder()
                        .setProductId(Constants.KEY_6_COIN)
                        .setProductType(INAPP)
                        .build()
        );
        return productList;
    }

    void verifyInAppPurchase(Purchase purchases) {
        AcknowledgePurchaseParams acknowledgePurchaseParams = AcknowledgePurchaseParams
                .newBuilder()
                .setPurchaseToken(purchases.getPurchaseToken())
                .build();
// lắng nghe trả về trạng thái mua
        billingClient.acknowledgePurchase(acknowledgePurchaseParams, billingResult -> {
            if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {
                MainApp.getInstance().getPreference().setPremium(1);
                String proId = purchases.getProducts().get(0);
                int quantity = purchases.getQuantity();
                setPurchaseResponse(this::setupResult);
                onPurchaseResponse.onResponse(proId, quantity);
                allowMultiplePurchases(purchases);
                Toast.makeText(PurchaseInAppActivity.this, "verifyInAppPurchase Mua ok--> " + proId, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void allowMultiplePurchases(Purchase purchase) {
        // gọi thằng này để mua nhiều lần
        ConsumeParams consumeParams = ConsumeParams
                .newBuilder()
                .setPurchaseToken(purchase.getPurchaseToken())
                .build();
        billingClient.consumeAsync(consumeParams, new ConsumeResponseListener() {
            @Override
            public void onConsumeResponse(BillingResult billingResult, String s) {
                Toast.makeText(PurchaseInAppActivity.this, " Resume item ", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onClickItem(ProductDetails item) {
        launchPurchaseFlow(item);
    }

    private void launchPurchaseFlow(ProductDetails productDetails) {
        ImmutableList<BillingFlowParams.ProductDetailsParams> productDetailsParamsList =
                ImmutableList.of(
                        BillingFlowParams.ProductDetailsParams.newBuilder()
                                .setProductDetails(productDetails)
                                .build()
                );
        BillingFlowParams billingFlowParams = BillingFlowParams.newBuilder()
                .setProductDetailsParamsList(productDetailsParamsList)
                .build();
        billingClient.launchBillingFlow(this, billingFlowParams);
    }

    protected void onResume() {
        super.onResume();
        billingClient.queryPurchasesAsync(
                QueryPurchasesParams.newBuilder().setProductType(INAPP).build(),
                (billingResult, list) -> {
                    if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {
                        for (Purchase purchase : list) {
                            if (purchase.getPurchaseState() == Purchase.PurchaseState.PURCHASED && !purchase.isAcknowledged()) {
                                verifyInAppPurchase(purchase);
                            }
                        }
                    }
                }
        );
    }

    private void setupResult(String proId, int quantity) {
        Intent intent = new Intent();
        int totalCoin = MainApp.getInstance().getPreference().getValueCoin();
        int remainCoin = totalCoin + getCoinFromKey(proId) * quantity;
        MainApp.getInstance().getPreference().setValueCoin(remainCoin);
        setResult(AppCompatActivity.RESULT_OK, intent);
        runOnUiThread(this::onBackPressed);

    }

    private int getCoinFromKey(String coin) {
        switch (coin) {
            case Constants.KEY_1_COIN:
                return 50;
            case Constants.KEY_2_COIN:
                return 100;
            case Constants.KEY_3_COIN:
                return 200;
            case Constants.KEY_4_COIN:
                return 400;
            case Constants.KEY_5_COIN:
                return 600;
            case Constants.KEY_6_COIN:
                return 700;
            default:
                return 0;
        }
    }

    interface OnPurchaseResponse {
        void onResponse(String proId, int quantity);
    }

    private void setPurchaseResponse(OnPurchaseResponse onPurchaseResponse) {
        this.onPurchaseResponse = onPurchaseResponse;
    }
}
