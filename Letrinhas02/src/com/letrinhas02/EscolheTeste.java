package com.letrinhas02;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.widget.ToggleButton;

public class EscolheTeste extends Activity {
	int nTestes=15;
	
	/**************************** Por Fazer ********************************++
	 * executar os testes selecionados, um de cada vez
	 */
	public void executarTestes(){
		LinearLayout ll = (LinearLayout)findViewById(R.id.llescteste);
		int nElements= ll.getChildCount();
		int j=0;
		for (int i=0;i<nElements; i++){
			//verificar se o teste est� ativo
			if(((ToggleButton)ll.getChildAt(i)).isPressed()){
				//inserir nume lista a criar o teste a realizar.
				j++;
			}	
		}
		
		Toast.makeText(getApplicationContext(),""+j+" Testes seleccionados", Toast.LENGTH_LONG).show();
		//iniciar os testes.... por fazer
		
		
		
	}
	
	/**
	 * Finalizar a ativity e voltar para a pagina anterior
	 */
	public void voltar(){
        finish();		
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.escolhe_teste);
		

		/****************** falta testar ************************************
		 * Tentativa de criar um painel din�mico para os bot�es de selec��o
		 * dos testes existentes.
		 * 
		 * � necess�rio de saber primeiro onde est�o os testes e quantos s�o!
		 */
		//aceder � BD local, contar o n� de testes e os seus t�tulos
		//guardar essa informa��o num array para se aceder na constru��o do scroll view
		
		//
		//ScrollView sv = (ScrollView)findViewById(R.id.svEscolher);
		LinearLayout ll = (LinearLayout)findViewById(R.id.llescteste);
		//Bot�o original que existe por defeni��o
		ToggleButton tg1 = (ToggleButton) findViewById(R.id.ToggleButton1);
		
		int i=0;
		for (i=0;i<nTestes;i++){		
			// um novo bot�o
			ToggleButton tg = new ToggleButton(getBaseContext());

			tg.setLayoutParams(tg1.getLayoutParams());
			// texto se n�o seleccionado
			tg.setTextOff("O t�tulo do teste");
			//texto se seleccionado
			tg.setTextOn(i+" - "+"O t�tulo do teste");
			//copiar os parametros de layout
			//inserir no scroll view		
			ll.addView(tg);
			//sv.addView(tg);
		}
			
		//escondo / elimino o bot�o original	
		tg1.setVisibility(View.INVISIBLE);
		tg1.destroyDrawingCache();
	
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
