package com.warehourse.app.ui.preview;

import com.biz.base.BaseViewHolder;
import com.warehourse.app.R;
import com.warehourse.app.model.entity.OrderEntity;

import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.TextView;

class InvoiceViewHolder extends BaseViewHolder {
    CheckedTextView text;
    TextView text1;
    EditText editInvoice, editInvoiceNumber;

    InvoiceViewHolder(View view) {
        super(view);
        text = getView(R.id.text);
        text1 = getView(R.id.text1);
        editInvoice = findViewById(R.id.edit_invoice);
        editInvoiceNumber = findViewById(R.id.edit_number);
        setCheckedChange();
        text.setOnClickListener(v -> {
            text.setChecked(!text.isChecked());
            setCheckedChange();
        });
    }

    private void setCheckedChange() {
        editInvoice.setVisibility(text.isChecked() ? View.VISIBLE : View.GONE);
        text1.setVisibility(text.isChecked() ? View.VISIBLE : View.GONE);
        editInvoiceNumber.setVisibility(text.isChecked() ? View.VISIBLE : View.GONE);
    }

    public boolean isChecked() {
        return text.isChecked();
    }

    public String getInvoiceTitle() {
        return editInvoice.getText().toString();
    }

    public String getInvoiceNUmber() {
        return editInvoiceNumber.getText().toString();
    }

    public static InvoiceViewHolder createViewHolder(ViewGroup parent) {
        return new InvoiceViewHolder(inflater(R.layout.item_invoice_layout, parent));
    }
}