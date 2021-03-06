package com.anequimplus.entity;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ContaPedidoItemCancelamento {
    private int id ;
    private String uuid ;
    private ContaPedidoItem contaPedidoItem ;
    private Date data ;
    private int usuario_id ;
    private double quantidade ;
    private int status ;

    public ContaPedidoItemCancelamento(int id, String uuid, ContaPedidoItem contaPedidoItem, Date data, int usuario_id, double quantidade, int status) {
        this.id = id;
        this.uuid = uuid;
        this.contaPedidoItem = contaPedidoItem;
        this.data = data;
        this.usuario_id = usuario_id;
        this.quantidade = quantidade;
        this.status = status;
    }

    public JSONObject getJSON() throws JSONException {
        JSONObject j = new JSONObject();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        j.put("CONTA_PEDIDO_ITEM_ID", contaPedidoItem.getId()) ;
        j.put("CONTA_PEDIDO_ITEM_UUID", contaPedidoItem.getUUID()) ;
        j.put("UUID", uuid) ;
        j.put("DATA", df.format(data)) ;
        j.put("QUANTIDADE", quantidade) ;
        j.put("STATUS", status) ;
        return j ;
        /*

        CONTA_PEDIDO_ITEM_ID int(11)
        CONTA_PEDIDO_ITEM_UUID varchar(60)
        LOJA_ID int(11)
        DATA datetime
        SYSTEM_USER_ID int(11)
        QUANTIDADE double
        STATUS int(11)
        CONF_TERMINAL_ID int(11)

         */

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public ContaPedidoItem getContaPedidoItem() {
        return contaPedidoItem;
    }

    public void setContaPedidoItem(ContaPedidoItem contaPedidoItem) {
        this.contaPedidoItem = contaPedidoItem;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public int getUsuario_id() {
        return usuario_id;
    }

    public void setUsuario_id(int usuario_id) {
        this.usuario_id = usuario_id;
    }

    public double getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(double quantidade) {
        this.quantidade = quantidade;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "ContaPedidoItemCancelamento{" +
                "id=" + id +
                ", uuid='" + uuid + '\'' +
                ", contaPedidoItem_id=" + contaPedidoItem.getId() +
                ", contaPedidoItem_uuid='" + contaPedidoItem.getUUID() + '\'' +
                ", data=" + data +
                ", usuario_id=" + usuario_id +
                ", quantidade=" + quantidade +
                ", status=" + status +
                '}';
    }
}
