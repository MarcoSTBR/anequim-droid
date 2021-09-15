package com.anequimplus.ado;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

    private Context ctx ;
    private static DBHelper DB = null ;

    public DBHelper(Context ctx) {
        super(ctx, "BbAnequimP", null, 6);
        this.ctx = ctx ;
    }

    public static DBHelper getDB(Context ctx){
        if (DB == null) {
            DB = new DBHelper(ctx) ;
        }
        return  DB;
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        criarTabelas(db) ;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        if (i == 3) {
            if (i1 == 4){
                excluirTabelas(db) ;
                criarTabelas(db) ;
            }
        }
        if (i < 5) {
            if (i1 == 5){
                excluirTabelas(db) ;
                criarTabelas(db) ;
            }
        }
        if (i < 6) {
            if (i1 == 6){
                excluirTabelas(db) ;
                criarTabelas(db) ;
            }
        }
    }

    public void excluirTabelas(SQLiteDatabase db){
        db.execSQL("DROP TABLE IF EXISTS LINKACESSO ") ;
        db.execSQL("DROP TABLE IF EXISTS GRUPO ") ;
        db.execSQL("DROP TABLE IF EXISTS PRODUTO ") ;
        db.execSQL("DROP TABLE IF EXISTS PEDIDO ") ;
        db.execSQL("DROP TABLE IF EXISTS PEDIDO_ITEM ") ;
        db.execSQL("DROP TABLE IF EXISTS FUNCIONARIO ") ;
        db.execSQL("DROP TABLE IF EXISTS PEDIDO_FUNCIONARIO ") ;
        db.execSQL("DROP TABLE IF EXISTS PEDIDO_FUNC_ITEM ") ;
        db.execSQL("DROP TABLE IF EXISTS EMPRESA ") ;
        db.execSQL("DROP TABLE IF EXISTS CENTROCUSTO ") ;
        db.execSQL("DROP TABLE IF EXISTS FUNCIONARIO_TIPO ") ;
        db.execSQL("DROP TABLE IF EXISTS VENDA_VISTA") ;
        db.execSQL("DROP TABLE IF EXISTS VENDA_VISTA_ITEM") ;
        db.execSQL("DROP TABLE IF EXISTS VENDA_VISTA_PAGAMENTO") ;
        db.execSQL("DROP TABLE IF EXISTS MODALIDADE") ;
        db.execSQL("DROP TABLE IF EXISTS LOJA") ;
    }

    public static void criarTabelas(SQLiteDatabase db){
        db.execSQL("CREATE TABLE IF NOT EXISTS LINKACESSO ( "
                + "ID INTEGER , "
                + "LINK TEXT, "
                + "URL TEXT)");

        db.execSQL("CREATE TABLE IF NOT EXISTS GRUPO ( "
                + "ID INTEGER , "
                + "DESCRICAO TEXT,"
                + "STATUS INTEGER)");

        db.execSQL("CREATE TABLE IF NOT EXISTS PRODUTO ( "
                + "ID INTEGER , "
                + "GRUPO_ID INTEGER , "
                + "CODIGO TEXT , "
                + "UNIDADE TEXT , "
                + "DESCRICAO TEXT , "
                + "PRECO DOUBLE , "
                + "STATUS INTEGER,"
                + "URL TEXT, "
                + "PARAM TEXT)");

        db.execSQL("CREATE TABLE IF NOT EXISTS PEDIDO ( "
                + "ID INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "PEDIDO TEXT , "
                + "DATA DATETIME) ");

        db.execSQL("CREATE TABLE IF NOT EXISTS PEDIDO_ITEM ( "
                + "ID INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "PEDIDO_ID INTEGER, "
                + "PRODUTO_ID INTEGER, "
                + "QUANTIDADE DOUBLE, "
                + "PRECO DOUBLE, "
                + "DESCONTO DOUBLE, "
                + "VALOR DOUBLE, "
                + "OBS TEXT) ");

        db.execSQL("CREATE TABLE IF NOT EXISTS FUNCIONARIO ( "
                + "ID INTEGER, "
                + "EMPRESA_ID INTEGER, "
                + "CENTRO_CUSTO_ID INTEGER, "
                + "TIPO_ID INTEGER, "
                + "NOME TEXT, "
                + "STATUS INTEGER) ");

        db.execSQL("CREATE TABLE IF NOT EXISTS EMPRESA ( "
                + "ID INTEGER, "
                + "DESCRICAO TEXT)");

        db.execSQL("CREATE TABLE IF NOT EXISTS CENTROCUSTO ( "
                + "ID INTEGER, "
                + "DESCRICAO TEXT)");

        db.execSQL("CREATE TABLE IF NOT EXISTS FUNCIONARIO_TIPO ( "
                + "ID INTEGER, "
                + "DESCRICAO TEXT)");


        db.execSQL("CREATE TABLE IF NOT EXISTS PEDIDO_FUNCIONARIO ( "
                + "ID INTEGER, "
                + "FUNCIONARIO_ID INTEGER, "
                + "DATA DATETIME,"
                + "STATUS INTEGER)");

        db.execSQL("CREATE TABLE IF NOT EXISTS PEDIDO_FUNC_ITEM ( "
                + "ID INTEGER, "
                + "PEDIDO_FUNC_ID INTEGER, "
                + "PRODUTO_ID INTEGER, "
                + "QUANTIDADE DOUBLE, "
                + "PRECO DOUBLE, "
                + "VALOR DOUBLE, "
                + "DESCONTO DOUBLE, "
                + "STATUS INTEGER, "
                + "OBS TEXT) ");


        db.execSQL("CREATE TABLE IF NOT EXISTS VENDA_VISTA ( "
                + "ID INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "CAIXA_ID INTEGER, "
                + "CODIGO TEXT, "
                + "STATUS INTEGER,"
                + "DATA DATETIME)") ;

        db.execSQL("CREATE TABLE IF NOT EXISTS VENDA_VISTA_ITEM ( "
                + "ID INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "VENDA_VISTA_ID INTEGER, "
                + "PRODUTO_ID INTEGER, "
                + "QUANTIDADE DOUBLE, "
                + "PRECO DOUBLE, "
                + "VALOR DOUBLE, "
                + "DESCONTO DOUBLE, "
                + "STATUS INTEGER,"
                + "OBS TEXT)") ;

        db.execSQL("CREATE TABLE IF NOT EXISTS VENDA_VISTA_PAGAMENTO ( "
                + "ID INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "VENDA_VISTA_ID INTEGER, "
                + "MODALIDADE_ID INTEGER, "
                + "STATUS INTEGER, "
                + "VALOR DOUBLE)") ;

        db.execSQL("CREATE TABLE IF NOT EXISTS MODALIDADE ( "
                + "ID INTEGER, "
                + "CODIGO TEXT, "
                + "DESCRICAO TEXT, "
                + "TIPOMODALIDADE TEXT, "
                + "COD_RECEBIMENTO INTEGER, "
                + "URL TEXT, "
                + "PARAM TEXT,"
                + "STATUS INTEGER)") ;

        db.execSQL("CREATE TABLE IF NOT EXISTS LOJA ( "
                + "ID INTEGER, "
                + "NOME TEXT, "
                + "STATUS TEXT) ") ;
    }

}
