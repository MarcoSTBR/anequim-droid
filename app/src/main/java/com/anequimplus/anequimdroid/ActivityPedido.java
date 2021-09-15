package com.anequimplus.anequimdroid;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.anequimplus.adapter.PedidoAdapter;
import com.anequimplus.ado.Dao;
import com.anequimplus.conexoes.ComexaoImpressoraRemota;
import com.anequimplus.conexoes.ConexaoEnvioPedido;
import com.anequimplus.entity.Pedido;
import com.anequimplus.tipos.Link;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;

import java.util.List;

public class ActivityPedido extends AppCompatActivity {

    private EditText editPedido ;
    private ListView listPedido ;
    private List<Pedido> pedidoList ;
    private Pedido pedido = null ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pedido);
        Toolbar toolbar = findViewById(R.id.toolbar);
        //toolbar.setTitle("Faça o pedido");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                enviarPedidos() ;
            }
        });

        editPedido = findViewById(R.id.editTextPedido);
        editPedido.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if (keyEvent.getAction() == KeyEvent.ACTION_DOWN) {
                    if (i == KeyEvent.KEYCODE_DPAD_CENTER || i == KeyEvent.KEYCODE_ENTER) {
                        setPedido(editPedido.getText());
                        return true;
                    }
                }
                return false;
            }
        });

        listPedido = findViewById(R.id.listPedido);
        listPedido.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                editPedido.setText(pedidoList.get(i).getPedido()) ;
                addPedido();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        editPedido.setText("");
        display() ;
    }

    private void display(){
        pedidoList =  Dao.getPedidoADO(getBaseContext()).getList() ;
        listPedido.setAdapter(new PedidoAdapter(getBaseContext(), pedidoList));
    }

    private void enviarPedidos() {
        try {
            new ConexaoEnvioPedido(this) {
                @Override
                public void EnvioOK(JSONArray jr) throws Exception {
                    display() ;
                    new ComexaoImpressoraRemota(getBaseContext(), Link.fImpressoraRemotaPedido, jr){

                        @Override
                        public void oK(JSONArray jrr) {
                           // Toast.makeText(getBaseContext(),jrr.toString(), Toast.LENGTH_LONG).show();
                            alert(jrr.toString());
                        }

                        @Override
                        public void erro(String msg) {
                            alert(msg);
                        }
                    }.execute() ;
                }

                @Override
                public void ErroEnvio(String msg) {
                    setAlert(msg) ;
                }

            }.execute() ;
        } catch (Exception e) {
            e.printStackTrace();
            setAlert(e.getMessage()) ;
        }
    }

    private void alert(String txt){
        new AlertDialog.Builder(this)
                .setIcon(R.drawable.ic_notifications_black_24dp)
                .setTitle("Retorno:")
                .setMessage(txt)
                .setCancelable(false)
                .setPositiveButton("Ok",null).show();
    }

    private void setAlert(String txt){
        new AlertDialog.Builder(this)
                .setTitle("Alerta")
                .setMessage(txt)
                .setPositiveButton("OK",null).show();
    }

    public void setPedido(Editable p){
        if (editPedido.getText().toString().equals("")){
            setAlert("Pedido inválido!") ;
        } else  addPedido();
    }

    private void addPedido(){
        Dao.getItemSelectADO(getBaseContext()).getList().clear();
        pedido = Dao.getPedidoADO(getBaseContext()).getPedido(editPedido.getText().toString()) ;
        Intent intent = new Intent(getBaseContext(), ActivityEnvioPedido.class) ;
        Bundle params = new Bundle() ;
        params.putInt("PEDIDO_ID", pedido.getId());
        intent.putExtras(params) ;
        startActivity(intent);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_pedido, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home){
            finish();
        }
        if (item.getItemId() == R.id.action_conf) {
            startActivity(new Intent(getBaseContext(), ActivityConfiguracao.class));
            return true;
        }
        if (item.getItemId() == R.id.action_logout) {
            startActivity(new Intent(getBaseContext(), ActivityLogin.class));
            return true;
        }
        if (item.getItemId() == R.id.action_pedido_ok) {
            setPedido(editPedido.getText());
            return true;
        }
        return true ; //super.onOptionsItemSelected(item);
    }


}