package com.letrinhas02;

import com.letrinhas02.util.ExecutaTestes;

import android.app.Activity;
import android.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

public class EscolheTeste extends Activity {
	//modo prof, tvmoAluno = #5ddfff
	ImageButton volt, exect;
	int nTestes = 15;
	String titulos[];
	boolean modo;
	//Ender�o/Query dos testes [];

	/****************************
	 * Por Fazer ******************************** executar os testes
	 * selecionados, um de cada vez
	 */
	public void executarTestes() {
		LinearLayout ll = (LinearLayout) findViewById(R.id.llescteste);
		int nElements = ll.getChildCount();
		
		int j = 0;
		//descobrir quantos e quais foram selecionados
		for (int i = 0; i < nElements; i++) {
			// verificar se o teste est� ativo
			if (((ToggleButton) ll.getChildAt(i)).isChecked()) {
				// inserir numa lista a criar o teste a realizar.
				// por fazer ********************************************+
				j++;
			}
		}

		//mostra quantos foram pressionados
		Toast.makeText(getApplicationContext(),
				"" + j + " Testes seleccionados", Toast.LENGTH_SHORT).show();
		// iniciar os testes.... por fazer
		
		if(
		ExecutaTestes exect = new ExecutaTestes(this, modo);
		exect.start();

	}

	/**
	 * Finalizar a ativity e voltar para a pagina anterior
	 */
	public void voltar() {
		finish();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.escolhe_teste);

		/************************************************************************
		 * Cria��o de um painel din�mico para os bot�es de selec��o dos testes
		 * existentes.
		 * 
		 * � necess�rio de saber primeiro onde est�o os testes e quantos s�o!
		 * (Comunicar com a BD)
		 */
		// aceder � BD local, contar o n� de testes e os seus t�tulos
		// guardar essa informa��o num array para se aceder na constru��o do
		// scroll view

		//
		// ScrollView sv = (ScrollView)findViewById(R.id.svEscolher);
		LinearLayout ll = (LinearLayout) findViewById(R.id.llescteste);
		// Bot�o original que existe por defeni��o
		ToggleButton tg1 = (ToggleButton) findViewById(R.id.ToggleButton1);

		// Se o n� de testes for superior a 0, cria o n� de bot�es referentes aos testes
		if (0<nTestes) {
			int i = 0;
			// Atribuo o primeiro t�tulo ao primeiro bot�o
			// texto por defeito
			tg1.setText("O t�tulo do teste");
			// texto se n�o seleccionado = "titulo do teste sem numera��o"
			tg1.setTextOff("O t�tulo do teste");
			// texto se seleccionado = "titulo do teste com numera��o"
			tg1.setTextOn((i+1) + " - " + "O t�tulo do teste");
			i++;

			//Resto do t�tulos
			while(i<nTestes){
				// um novo bot�o
				ToggleButton tg = new ToggleButton(getBaseContext());
				// copiar os parametros de layout do 1� bot�o
				tg.setLayoutParams(tg1.getLayoutParams());
				tg.setTextSize(tg1.getTextSize());				
				// texto por defeito
				tg.setText("O t�tulo do teste");
				// texto se n�o seleccionado = "titulo do teste sem numera��o"
				tg.setTextOff("O t�tulo do teste");
				// texto se seleccionado = "titulo do teste com numera��o"
				tg.setTextOn((i+1) + " - " + "O t�tulo do teste");				
				// inserir no scroll view
				ll.addView(tg);
				i++;
			}
		}
		else{
			
		}

		volt = (ImageButton) findViewById(R.id.escTVoltar);
		exect = (ImageButton) findViewById(R.id.ibComecar);
		
		escutaBotoes();
	}
	
	private void escutaBotoes(){
        exect.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                    	executarTestes();
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


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.escolhe_teste, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.escolhe_teste, container,
					false);
			return rootView;
		}
	}

}
