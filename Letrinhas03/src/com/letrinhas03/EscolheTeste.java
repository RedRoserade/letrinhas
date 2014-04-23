package com.letrinhas03;

import com.letrinhas03.util.SystemUiHider;
import com.letrinhas03.util.Teste;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.widget.ToggleButton;

public class EscolheTeste extends Activity {
	ImageButton volt, exect;
	public int nTestes;
	boolean modo;
	Teste[] teste;
	Teste[] lista;

	/*********************************************************************
	 * The flags to pass to {@link SystemUiHider#getInstance}.
	 */
	private static final int HIDER_FLAGS = SystemUiHider.FLAG_HIDE_NAVIGATION;
	/**
	 * The instance of the {@link SystemUiHider} for this activity.
	 */
	private SystemUiHider mSystemUiHider;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.escolhe_teste);
		// recebe o parametro de modo
		Bundle b = getIntent().getExtras();
		modo = b.getBoolean("Modo");

		volt = (ImageButton) findViewById(R.id.escTVoltar);
		exect = (ImageButton) findViewById(R.id.ibComecar);
		// esconder o title************************************************+
		final View contentView = findViewById(R.id.escTeste);

		// Set up an instance of SystemUiHider to control the system UI for
		// this activity.
		mSystemUiHider = SystemUiHider.getInstance(this, contentView,
				HIDER_FLAGS);
		mSystemUiHider.setup();
		mSystemUiHider.hide();

		/************************************************************************
		 * Cria��o de um painel din�mico para os bot�es de selec��o dos testes
		 * existentes.
		 * 
		 * � necess�rio de saber primeiro onde est�o os testes e quantos s�o!
		 * (Comunicar com a BD)
		 * 
		 * aceder � BD local, contar o n� de testes, ex: nTestes
		 * ="Conta(bl�.bl�.bl�)" ; teste= new Teste[nTestes];
		 * 
		 * e os seus t�tulos guardar essa informa��o num array para se aceder na
		 * constru��o do scroll view. ex: for(int i=0;i<teste.length i++){ int
		 * tipo= "tipo(bl�.bl�.bl�)"; String tit = "titulo(bl�bl�bl�)"; String
		 * ender = "endere�o(bl�bl�bl�)"; teste[i]=new Teste(tip,tit,ender); }
		 * 
		 */

		// teste:::::::::::
		nTestes = 30;
		teste = new Teste[nTestes];
		for (int i = 0; i < teste.length; i++) {
			int tip = 0; // tipo texto
			String tit = "O t�tulo do teste";
			teste[i] = new Teste(i, tip, tit);
		}// :::::::::::::::::::::::::::::::::::::::::::

		// Painel din�mico ****************************************************
		LinearLayout ll = (LinearLayout) findViewById(R.id.llescteste);
		// Bot�o original que existe por defeni��o
		ToggleButton tg1 = (ToggleButton) findViewById(R.id.ToggleButton1);

		// Se existirem testes no reposit�rio correspondentes, cria o n� de
		// bot�es referentes ao n� de testes existentes
		if (0 < nTestes) {
			int i = 0;
			// Atribuo o primeiro t�tulo ao primeiro bot�o
			// ********************************+
			// texto por defeito
			tg1.setText(teste[i].getTitulo());
			// texto se n�o seleccionado = "titulo do teste sem numera��o"
			tg1.setTextOff(teste[i].getTitulo());
			// texto se seleccionado = "titulo do teste com numera��o"
			tg1.setTextOn((i + 1) + " - " + teste[i].getTitulo());
			i++;

			// Resto do t�tulos
			while (i < nTestes) {
				// um novo bot�o
				ToggleButton tg = new ToggleButton(getBaseContext());
				// copiar os parametros de layout do 1� bot�o
				tg.setLayoutParams(tg1.getLayoutParams());
				tg.setTextSize(tg1.getTextSize());
				// texto por defeito
				tg.setText(teste[i].getTitulo());
				// texto se n�o seleccionado = "titulo do teste sem numera��o"
				tg.setTextOff(teste[i].getTitulo());
				// texto se seleccionado = "titulo do teste com numera��o"
				tg.setTextOn((i + 1) + " - " + teste[i].getTitulo());
				// inserir no scroll view
				ll.addView(tg);
				i++;
			}
		} else {
			android.app.AlertDialog alerta;
			// Cria o gerador do AlertDialog
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			// define o titulo
			builder.setTitle("Letrinhas 03");
			// define a mensagem
			builder.setMessage("N�o foram encontrados testes no reposit�rio");
			// define um bot�o como positivo
			builder.setPositiveButton("OK", null);
			// cria o AlertDialog
			alerta = builder.create();
			// Mostra
			alerta.show();

			// esconder os bot�es
			tg1.setVisibility(View.INVISIBLE);
			exect.setVisibility(View.INVISIBLE);

		}

		volt = (ImageButton) findViewById(R.id.escTVoltar);
		exect = (ImageButton) findViewById(R.id.ibComecar);

		escutaBotoes();
	}

	/**
	 * Procedimento para voltar a esconder o titulo caso este esteja activo
	 * 
	 * @author Thiago
	 */
	@Override
	public boolean hasWindowFocus() {
		// esconder o title
		final View contentView = findViewById(R.id.escTeste);
		// Set up an instance of SystemUiHider to control the system UI for
		// this activity.
		mSystemUiHider = SystemUiHider.getInstance(this, contentView,
				HIDER_FLAGS);
		mSystemUiHider.setup();
		mSystemUiHider.hide();
		return true;
	}

	/**
	 * Procedimento para veirficar os bot�es
	 * 
	 * @author Thiago
	 */
	private void escutaBotoes() {
		exect.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				executarTestes();
			}
		});

		volt.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {// sair da aplica��o
				finish();
			}
		});
	}

	/**************************************************************************
	 * Por Fazer ******************************** executar os testes
	 * selecionados, um de cada vez
	 */
	public void executarTestes() {
		LinearLayout ll = (LinearLayout) findViewById(R.id.llescteste);
		// contar o n� de elementos (testes)
		int nElements = ll.getChildCount();

		int j = 0;
		// contar quantos e quais foram selecionados
		for (int i = 0; i < nElements; i++) {
			// verificar se o teste est� ativo
			if (((ToggleButton) ll.getChildAt(i)).isChecked()) {
				j++;
			}
		}
		Toast.makeText(getApplicationContext(), j + " Testes seleccionados",
				Toast.LENGTH_SHORT).show();

		// Copiar os testes seleccionados para uma lista auxiliar
		lista = new Teste[j];
		j = 0;
		for (int i = 0; i < nElements; i++) {
			if (((ToggleButton) ll.getChildAt(i)).isChecked()) {
				lista[j] = teste[i];
				j++;
			}
		}

		// iniciar os testes....
		// Se existe items seleccionados arranca com os testes,
		if (0 < j) {
			//Decompor o array de teste, para poder enviar por parametros
			int[] lstID = new int[lista.length];
			int[] lstTipo = new int[lista.length];
			String[] lstTitulo = new String[lista.length];
			for (int i = 0; i < lista.length; i++) {
				lstID[i] = lista[i].getID();
				lstTipo[i] = lista[i].getTipo();
				lstTitulo[i] = lista[i].getTitulo();
			}

			switch (lista[0].getTipo()) {
			case 0:
				
				// lan�ar a nova activity do tipo texto,
				// enviar o parametro de modo
				Bundle wrap = new Bundle();
				wrap.putBoolean("Modo", modo);

				// teste, a depender das informa��es da BD
				// ****************************
				wrap.putString("Aluno", "EI3C-Tiago Fernandes");
				wrap.putString("Professor", "ESTT-Antonio Manso");

				wrap.putIntArray("ListaID", lstID);
				wrap.putIntArray("ListaTipo", lstTipo);
				wrap.putStringArray("ListaTitulo", lstTitulo);

				// iniciar a pagina 2 (escolher teste)
				Intent it = new Intent(getApplicationContext(),
						Teste_Texto.class);
				it.putExtras(wrap);

				startActivity(it);

				// while(isRunning);

				// Toast.makeText(null, "TEste acabou!!!",
				// Toast.LENGTH_LONG).show();

				break;
			case 1:

				Toast.makeText(getApplicationContext(), "" + 1 + " - Palavras",
						Toast.LENGTH_SHORT).show();

				// lan�ar a nova activity do tipo Palavras, e o seu conte�do
				//
				// Intent it = new
				// Intent(act.getApplicationContext(),texto.class);
				// act.startActivity(it);

				// esperar que esta termine
				// while (!act.isDestroyed());

				break;
			case 2:
				Toast.makeText(getApplicationContext(), 2 + " - Poemas",
						Toast.LENGTH_SHORT).show();
				// lan�ar a nova activity do tipo Poema, e o seu conte�do
				//
				//

				break;
			case 3:
				Toast.makeText(getApplicationContext(), 3 + " - Imagens",
						Toast.LENGTH_SHORT).show();
				// lan�ar a nova activity do tipo imagem, e o seu conte�do
				//
				//

				break;
			default:
				Toast.makeText(getApplicationContext(), " - Tipo n�o defenido",
						Toast.LENGTH_SHORT).show();
				// n�o lan�ar nada e continuar

				break;
			}

		} else {// sen�o avisa que n�o existe nada seleccionado
			android.app.AlertDialog alerta;
			// Cria o gerador do AlertDialog
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			// define o titulo
			builder.setTitle("Letrinhas 03");
			// define a mensagem
			builder.setMessage("N�o existem testes seleccionados!");
			// define um bot�o como positivo
			builder.setPositiveButton("OK", null);
			// cria o AlertDialog
			alerta = builder.create();
			// Mostra
			alerta.show();
		}

	}

}
