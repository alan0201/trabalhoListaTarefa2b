package com.example.listatarefab.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.listatarefab.model.Tarefa;

import java.util.ArrayList;
import java.util.List;

public class TarefaDAO implements ITarefaDAO {

    private SQLiteDatabase le;
    private SQLiteDatabase escreve;


    public TarefaDAO(Context context) {
        DbHelper db = new DbHelper(context);
        le = db.getReadableDatabase();
        escreve = db.getWritableDatabase();
    }

    @Override
    public boolean salvar(Tarefa tarefa) {
        ContentValues cv = new ContentValues();
        cv.put("nome", tarefa.getNomeTarefa() );

        try {
            escreve.insert(DbHelper.TABELA_TAREFAS, null, cv);
            Log.i("RESULTADO","Tarefa salvar com sucesso");

        }catch (Exception e){
            Log.i("RESULTADO", "Erro ao salvar tarefa");
            return false;
        }

        return true;
    }

    @Override
    public boolean atualizar(Tarefa tarefa) {
        ContentValues cv = new ContentValues();
        cv.put("nome", tarefa.getNomeTarefa() );

        try {
            String[] agrs = {tarefa.getId().toString()};
            escreve.update(DbHelper.TABELA_TAREFAS,cv, "id=?",agrs);
            Log.i("RESULTADO","Sucesso ao atualizar tarefa");

        }catch (Exception e){
            Log.i("RESULTADO", "Erro ao atualizar tarefa");
            return false;
        }

        return true;

    }

    @Override
    public boolean deletar(Tarefa tarefa) {

        try {
            String[] args = {tarefa.getId().toString()};
            escreve.delete(DbHelper.TABELA_TAREFAS, "id=?",args);
            Log.i("RESULTADO", "Sucesso ao removere tarefa");

        }catch (Exception e){
            Log.i("RESULTADO", "erro ao removere tarefa");
            return false;

        }

        return true;
    }

    @Override
    public List<Tarefa> lista() {
        List<Tarefa> tarefas =  new ArrayList<>();
        String sql = "SELECT * FROM "+DbHelper.TABELA_TAREFAS+";";
        Cursor c = le.rawQuery(sql, null);

        while(c.moveToNext()){

            Tarefa tarefa = new Tarefa();

            Long id = c.getLong(c.getColumnIndex("id"));
            String nomeTarefa = c.getString(c.getColumnIndex("nome"));

            tarefa.setId(id);
            tarefa.setNomeTarefa(nomeTarefa);

            tarefas.add(tarefa);
        }



        return tarefas;
    }
}
