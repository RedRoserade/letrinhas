package com.letrinhas02;

import com.letrinhas02.util.SystemUiHider;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 * 
 * @see SystemUiHider
 */
public class EscModo extends Activity {
	ImageButton aluno,prof,volt;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.esc_modo);

		aluno = (ImageButton) findViewById(R.id.ecmAluno);
		prof = (ImageButton) findViewById(R.id.esmProf);
		volt = (ImageButton) findViewById(R.id.escmVoltar);

		escutaBotoes();
	}
	
	private void escutaBotoes(){
        aluno.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        modAluno();
                    }
                }

        );
        
        prof.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        modProf();
                    }
                }

        );

        volt.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //sair da aplica��o
                        finish();
                    }
                }
        );
	}

	

	public void modAluno(){
		//declarar de como o teste ser� apresentado!
		Toast.makeText(getApplicationContext(),"Entrar no teste em modo de Aluno", Toast.LENGTH_LONG).show();
		 //iniciar a pagina 2 (escolher teste)
        Intent it= new Intent(EscModo.this,EscolheTeste.class);
        startActivity(it);
        
    	//Toast.makeText(getApplicationContext(),"Ir� muda de p�gina", Toast.LENGTH_LONG).show();
		finish();
	}

	public void modProf(){
		//declarar de como o teste ser� apresentado!
		Toast.makeText(getApplicationContext(),"Entrar no teste em modo de Professor", Toast.LENGTH_LONG).show();
		 //iniciar a pagina 2 (escolher teste)
        Intent it= new Intent(EscModo.this,EscolheTeste.class);
        startActivity(it);
        
    	//Toast.makeText(getApplicationContext(),"Ir� muda de p�gina", Toast.LENGTH_LONG).show();
		finish();
	}

	public void voltar(){
		finish();
	}

}