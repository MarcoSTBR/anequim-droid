package com.anequimplus.anequimdroid;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.anequimplus.adapter.OpcoesFechamentoAdapter;
import com.anequimplus.ado.Dao;
import com.anequimplus.ado.LinkAcessoADO;
import com.anequimplus.conexoes.ConexaoCaixa;
import com.anequimplus.conexoes.ConexaoOpFechamento;
import com.anequimplus.entity.Caixa;
import com.anequimplus.entity.OpcoesFechamento;
import com.anequimplus.utilitarios.UtilSet;

import java.net.MalformedURLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ActivityFechamentoCaixa extends AppCompatActivity {

    private Toolbar toolbar ;
    private Caixa caixa ;
    private RecyclerView lista ;
    private OpcoesFechamento opcoesFechamento ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fechamento_caixa);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        lista = findViewById(R.id.listaOpcoesFechamento) ;

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_fechamento, menu);
        return true ;//super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            finish();
        }
        if ((item.getItemId() == R.id.action_fechar)){
            setFechar() ;
            return true ;
        }
        return true ;
    }

    private void setFechar() {
        new AlertDialog.Builder(this)
                .setIcon(R.drawable.ic_notifications_black_24dp)
                .setTitle("Aten????o")
                .setMessage("Deseja fechar o movimento do caixa?")
                .setCancelable(false)
                .setNegativeButton("N??o",null)
                .setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        setCaixa() ;
                    }
                }).show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getCaixa() ;
      //  getFechamentos();
        //getConsultaFechamento() ;
        //carregaImpressora();

    }
    public void setCaixa(){
        List<Caixa> l = new ArrayList<Caixa>() ;
        caixa.setStatus(2);
        caixa.setData(new Date());
        l.add(caixa) ;

        try {
            new ConexaoCaixa(this, l){

                @Override
                public void Ok(List<Caixa> l) {
                    for (Caixa c : l){
                        if (c.getUuid().equals(caixa.getUuid())){
                            Dao.getCaixaADO(getBaseContext()).alterar(caixa);
                        }
                    }


                }

                @Override
                public void erro(String msg) {
                    alertaErro(msg) ;


                }
            }.execute();
        } catch (MalformedURLException e) {
            e.printStackTrace();
            alertaErro(e.getMessage());
        } catch (LinkAcessoADO.ExceptionLinkNaoEncontrado e) {
            alertaErro(e.getMessage());
            e.printStackTrace();
        }


    }
    private void getCaixa() {
        caixa = Dao.getCaixaADO(this).getCaixaAberto(UtilSet.getUsuarioId(this));
        if (caixa  == null){
            new AlertDialog.Builder(this)
                    .setIcon(R.drawable.ic_notifications_black_24dp)
                    .setTitle("Aten????o")
                    .setMessage("Caixa Fechado!")
                    .setCancelable(false)
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            finish();
                        }
                    }).show();
        } else {
            getMenuFechamento() ;
        }
    }

    private void getMenuFechamento(){
        try {
            new ConexaoOpFechamento(this, caixa) {
                @Override
                public void oK(Caixa c, List<OpcoesFechamento> list) {
                    //caixa = c ;
                    toolbar.setTitle("Fechamento Caixa");
                    SimpleDateFormat fdate = new SimpleDateFormat("E dd/MM/yyyy hh:mm:ss");
                    toolbar.setSubtitle(fdate.format(caixa.getData()));

                    OpcoesFechamentoAdapter Adapter = new OpcoesFechamentoAdapter(list) ;
                    Adapter.setOnItemClickListener(new OpcoesFechamentoAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(int position) {
                            setVisualizarRelatorio(position) ;
                        }
                    });
                    lista.setLayoutManager(new LinearLayoutManager(getBaseContext()));
                    lista.setHasFixedSize(true);
                    lista.setAdapter(Adapter) ;

                }

                @Override
                public void erro(String msg) {
                    alertaErro(msg) ;

                }

            }.execute() ;
        } catch (LinkAcessoADO.ExceptionLinkNaoEncontrado | MalformedURLException e) {
            e.printStackTrace();
            alertaErro(e.getMessage()) ;

        }

    }


    private void setVisualizarRelatorio(int position) {
        Log.i("setVisualizarRelatorio", "position "+position) ;
        opcoesFechamento = Dao.getOpcoesFechamentoADO(getBaseContext()).getList().get(position) ;
        Intent intent = new Intent(getBaseContext(), ActivityImpressao.class);
        Bundle params = new Bundle();
        SimpleDateFormat dt = new SimpleDateFormat("E, dd/MM/yy hh:mm");
        params.putInt("OPCAO_ID", opcoesFechamento.getId());
        params.putInt("CAIXA_ID", caixa.getId());
        params.putString("TITULO", opcoesFechamento.getDescricao());
        params.putString("SUBTITULO", "Abertura "+dt.format(caixa.getData()));

        intent.putExtras(params);
        startActivity(intent) ;
    }


    private void alertaErro(String txt){
        new AlertDialog.Builder(this)
                .setIcon(R.drawable.ic_notifications_black_24dp)
                .setTitle("Aten????o")
                .setMessage(txt)
                .setCancelable(false)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                    }
                }).show();
    }


}
