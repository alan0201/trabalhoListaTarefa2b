package com.example.listatarefab.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.listatarefab.R;
import com.example.listatarefab.adapter.TarefaAdapter;
import com.example.listatarefab.helper.RecyclerItemClickListener;
import com.example.listatarefab.helper.TarefaDAO;
import com.example.listatarefab.model.Tarefa;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {



    private RecyclerView recyclerView;
    private TarefaAdapter tarefaAdapter;
    private List<Tarefa> ListaTarefas = new ArrayList<>();
    private Tarefa tarefaSelecionda;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //configurar recyclerview
        recyclerView = findViewById(R.id.recyclerView);

        //adicionar evento de clique
        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getApplicationContext(), recyclerView,
                new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {

                        Tarefa tarefaSelecionada = ListaTarefas.get(position);

                        Intent intent = new Intent(getApplicationContext(), AdicionarTarefaActivity.class);
                        intent.putExtra("tarefaSelecionada", tarefaSelecionada);

                        startActivity(intent);

                    }

                    @Override
                    public void onLongItemClick(View view, int position) {
                        Log.i("clique:", "onLongClick");

                        tarefaSelecionda = ListaTarefas.get(position);

                        AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);

                        dialog.setTitle("Confirme Exclusão");
                        dialog.setMessage("Deseja excluir a tarefa " + tarefaSelecionda.getNomeTarefa() + "?");


                        dialog.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                TarefaDAO tarefaDAO = new TarefaDAO(getApplicationContext());
                                if(tarefaDAO.deletar(tarefaSelecionda)){
                                    carregarlistatarefas();
                                    Toast.makeText(getApplicationContext(), "Sucesso ao deletar tarefa!", Toast.LENGTH_SHORT).show();
                                }else{
                                    Toast.makeText(getApplicationContext(), "Erro ao deletar tarefa!", Toast.LENGTH_SHORT).show();
                                }



                            }
                        });
                        dialog.setNegativeButton("Não", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });

                        dialog.setNegativeButton("Não", null);

                        dialog.create();
                        dialog.show();

                    }


            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        }));



        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               Intent intent = new Intent(getApplicationContext(),AdicionarTarefaActivity.class);
               startActivity(intent);

            }
        });
    }

    //carregar lista de tarefas
    public void carregarlistatarefas(){
        //listar tarefas
        TarefaDAO tarefaDao = new TarefaDAO(getApplicationContext());
        ListaTarefas = tarefaDao.lista();


        //exibir lista de tarefas no recycleview

        //confidurar um adapter
        tarefaAdapter = new TarefaAdapter(ListaTarefas);

        //configurar o recyclerView
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new DividerItemDecoration(getApplicationContext(), LinearLayout.VERTICAL));
        recyclerView.setAdapter(tarefaAdapter);

    }

    @Override
    protected void onStart() {
        carregarlistatarefas();
        super.onStart();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
