package com.anequimplus.conexoes;

import android.content.Context;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.anequimplus.ado.Dao;
import com.anequimplus.ado.LinkAcessoADO;
import com.anequimplus.tipos.Link;
import com.anequimplus.utilitarios.TokenSet;
import com.anequimplus.utilitarios.UtilSet;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;

public abstract class ConexaoAutenticacao extends ConexaoServer {

    private String cnpj ;
    private String login ;
    private String password ;

    public ConexaoAutenticacao(Context ctx ,String cnpj, String login, String password) {
        super(ctx);
        token = "" ;
        try {
            this.cnpj = cnpj;
            this.login = login;
            this.password = password ;
            maps.put("class", "AfoodAutenticacao");
            maps.put("method", "autenticacao");
            maps.put("cnpj", this.cnpj);
            maps.put("login", this.login);
            maps.put("password", UtilSet.md5(ctx, this.password));
            msg = "Autenticação";
            url = Dao.getLinkAcessoADO(ctx).getLinkAcesso(Link.fAutenticacao).getUrl();
        } catch (MalformedURLException | LinkAcessoADO.ExceptionLinkNaoEncontrado e) {
            e.printStackTrace();
            erro(0, e.getMessage());
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        Log.e("ConexaoAutenticacarest",s) ;
        try {
            JSONObject j = new JSONObject(s);
            if (j.getString("status").equals("success")) {
                String token = j.optJSONObject("data").getString("access_token") ;
                UtilSet.setToken(ctx, token);
                UtilSet.setLojaId(ctx, TokenSet.getLojaId(ctx));
                UtilSet.setLojaNome(ctx, TokenSet.getLojaNome(ctx));
                UtilSet.setUsuarioId(ctx, TokenSet.getUsuarioId(ctx));
                UtilSet.setUsuarioNome(ctx, TokenSet.getUsuarioNome(ctx));
                UtilSet.setCnpj(ctx, cnpj);
                UtilSet.setLogin(ctx,login);
                UtilSet.setPassword(ctx, password);
                oK(cnpj, login);

                } else erro(codInt, j.getString("data"));
            } catch (JSONException e) {
                e.printStackTrace();
                erro(codInt, e.getMessage());
            }
    }

    public abstract void oK(String cnpj, String login) ;
    public abstract void erro(int cod, String msg) ;

}
