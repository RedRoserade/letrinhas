package com.letrinhas04.escolhe;

import java.util.List;

import com.letrinhas04.R;
import com.letrinhas04.BaseDados.LetrinhasDB;
import com.letrinhas04.ClassesObjs.Professor;
import com.letrinhas04.ClassesObjs.Turma;
import com.letrinhas04.R.id;
import com.letrinhas04.R.layout;
import com.letrinhas04.R.menu;
import com.letrinhas04.util.SystemUiHider;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.os.Build;

public class EscolheTurma extends Activity {
	Button volt;
	String Escola, Professor, FotoProf;
	int idEscola, idProfessor, nTurmas;

	/**
	 * Whether or not the system UI should be auto-hidden after
	 * {@link #AUTO_HIDE_DELAY_MILLIS} milliseconds.
	 */
	private static final boolean AUTO_HIDE = true;
	/**
	 * If {@link #AUTO_HIDE} is set, the number of milliseconds to wait after
	 * user interaction before hiding the system UI.
	 */
	private static final int AUTO_HIDE_DELAY_MILLIS = 1000;
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
		setContentView(R.layout.escolhe_turma);

		// Retirar os Extras

		Bundle b = getIntent().getExtras();
		// escola
		idEscola = b.getInt("Escola_ID");
		Escola = b.getString("Escola");
		((TextView) findViewById(R.id.escTEscola)).setText(Escola);

		// professor
		idProfessor = b.getInt("Professor_ID");
		Professor = b.getString("Professor");
		FotoProf =null;// b.getString("foto_Professor");
		((TextView) findViewById(R.id.tvTProf)).setText(Professor);
		
		ImageView imageView =((ImageView) findViewById(R.id.ivTProfessor));
		if (FotoProf != null) {
			String imageInSD = Environment
					.getExternalStorageDirectory().getAbsolutePath()
					+ "/School-Data/Professors/" + FotoProf;
			Bitmap bitmap = BitmapFactory.decodeFile(imageInSD);
			//imageView.setImageBitmap(bitmap);

			// ajustar o tamanho da imagem
			imageView.setImageBitmap(Bitmap.createScaledBitmap(bitmap,
					80, 80, false));
			// enviar para o bot�o
			//bt1.setCompoundDrawablesWithIntrinsicBounds(null,
				//	imageView.getDrawable(), null, null);
		} else {
			// sen�o copia a imagem do bot�o original
			///bt1.setCompoundDrawables(null,
			//		bt.getCompoundDrawablesRelative()[1], null, null);
		}
		
		

		// new line faz a rota��o do ecr�n em 180 graus
		int currentOrientation = getResources().getConfiguration().orientation;
		if (currentOrientation == Configuration.ORIENTATION_LANDSCAPE) {
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
		} else {
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);
		}

		// esconder o title************************************************+
		final View contentView = findViewById(R.id.escTurma);

		// Set up an instance of SystemUiHider to control the system UI for
		// this activity.
		mSystemUiHider = SystemUiHider.getInstance(this, contentView,
				HIDER_FLAGS);
		mSystemUiHider.setup();
		mSystemUiHider
				.setOnVisibilityChangeListener(new SystemUiHider.OnVisibilityChangeListener() {
					// Cached values.
					int mShortAnimTime;

					@Override
					@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
					public void onVisibilityChange(boolean visible) {
						if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
							if (mShortAnimTime == 0) {
								mShortAnimTime = getResources().getInteger(
										android.R.integer.config_shortAnimTime);
							}
						}

						if (visible && AUTO_HIDE) {
							// Schedule a hide().
							delayedHide(AUTO_HIDE_DELAY_MILLIS);
						}
					}
				});

		// Bot�o de voltar
		volt = (Button) findViewById(R.id.btnVoltarTurm);
		volt.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				// sair da activity
				finish();
			}
		});

		makeTabela();

	}

	/**
	 * Novo m�todo para criar o painel din�mico para os bot�es de selec��o da
	 * escola
	 * 
	 * @author Thiago
	 */
	@SuppressLint("NewApi")
	private void makeTabela() {

		// Cria o objecto da base de dados
		LetrinhasDB db = new LetrinhasDB(this);
		//************* Mudar este select de all para por ID de professor (ALEXANDRE!!)
		List<Turma> turmas = db.getAllTurmas();
		//*******************************************************************************
		nTurmas = turmas.size();
		int[] idTurmas = new int[turmas.size()];
		String nomeTurma[] = new String[turmas.size()];

		// preenche os arrays com a informa��o necess�ria
		for (int i = 0; i < nTurmas; i++) {
			idTurmas[i] = turmas.get(i).getId();
			nomeTurma[i] = turmas.get(i).getNome();
		}

		for (Turma cn : turmas) {
			String storage = cn.getAnoLetivo() + "," + cn.getAnoEscolar() + ","
					+ cn.getId() + "," + cn.getNome() + "," + cn.getIdEscola();
			Log.d("letrinhas-Turmas", storage.toString());

		}

		/**
		 * Scroll view com uma tabela de 4 colunas(max)
		 */
		// tabela a editar
		TableLayout tabela = (TableLayout) findViewById(R.id.tblEscolheTurm);
		// linha da tabela a editar
		TableRow linha = (TableRow) findViewById(R.id.Turmlinha01);
		// 1� bot�o
		Button bt = (Button) findViewById(R.id.TurmBtOriginal);
		bt.setText("teste turmas");

		// Contador de controlo
		int cont = 0;
		// criar o n� de linhas a dividir por 4 colunas
		for (int i = 0; i < nTurmas / 4; i++) {
			// nova linha da tabela
			TableRow linha1 = new TableRow(getBaseContext());
			// Copiar os parametros da 1� linha
			linha1.setLayoutParams(linha.getLayoutParams());
			// criar os 4 bot�es da linha
			for (int j = 0; j < 4; j++) {
				
				// **********************************
				// Nome da turma

				final String turm = nomeTurma[cont];
				final int idturm = idTurmas[cont];
				// ***********************************

				// novo bot�o
				Button bt1 = new Button(bt.getContext());
				// copiar os parametros do bot�o original
				bt1.setLayoutParams(bt.getLayoutParams());

				// copia a imagem do bot�o original
				bt1.setCompoundDrawables(null,
						bt.getCompoundDrawablesRelative()[1], null, null);

				// addicionar o nome
				bt1.setText(nomeTurma[cont]);
				// Defenir o que faz o bot�o ao clicar
				bt1.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						// Entrar na activity
						Bundle wrap = new Bundle();
						wrap.putString("Escola", Escola);
						wrap.putInt("Escola_ID", idEscola);
						wrap.putString("Professor", Professor);
						wrap.putInt("Professor_ID", idProfessor);
						wrap.putString("Turma", turm);
						wrap.putInt("Professor_ID", idturm);

						Intent it = new Intent(getApplicationContext(),
								EscolheAluno.class);
						it.putExtras(wrap);

						startActivity(it);
					}
				});
				// inserir o bot�o na linha
				linha1.addView(bt1);
				// incrementar o contador de controlo
				cont++;
			}
			// inserir a linha criada
			tabela.addView(linha1);
		}

		// resto
		if (nTurmas % 4 != 0) {
			TableRow linha1 = new TableRow(getBaseContext());
			linha1.setLayoutParams(linha.getLayoutParams());
			for (int j = 0; j < nTurmas % 4; j++) {

				// **********************************
				// Nome da turma

				final String turm = nomeTurma[cont];
				final int idturm = idTurmas[cont];
				// ***********************************

				// novo bot�o
				Button bt1 = new Button(bt.getContext());
				// copiar os parametros do bot�o original
				bt1.setLayoutParams(bt.getLayoutParams());

				// copia a imagem do bot�o original
				bt1.setCompoundDrawables(null,
						bt.getCompoundDrawablesRelative()[1], null, null);

				// addicionar o nome
				bt1.setText(nomeTurma[cont]);
				// Defenir o que faz o bot�o ao clicar
				bt1.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						// Entrar na activity
						Bundle wrap = new Bundle();
						wrap.putString("Escola", Escola);
						wrap.putInt("Escola_ID", idEscola);
						wrap.putString("Professor", Professor);
						wrap.putInt("Professor_ID", idProfessor);
						wrap.putString("Turma", turm);
						wrap.putInt("Professor_ID", idturm);

						Intent it = new Intent(getApplicationContext(),
								EscolheAluno.class);
						it.putExtras(wrap);

						startActivity(it);
					}
				});
				// inserir o bot�o na linha
				linha1.addView(bt1);
				// incrementar o contador de controlo
				cont++;
			}
			// inserir a linha criada
			tabela.addView(linha1); 
		}

		// por fim escondo a 1� linha
		tabela.removeView(linha);
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);

		// Trigger the initial hide() shortly after the activity has been
		// created, to briefly hint to the user that UI controls
		// are available.
		delayedHide(1000);
	}

	/**
	 * Touch listener to use for in-layout UI controls to delay hiding the
	 * system UI. This is to prevent the jarring behavior of controls going away
	 * while interacting with activity UI.
	 */
	View.OnTouchListener mDelayHideTouchListener = new View.OnTouchListener() {
		@Override
		public boolean onTouch(View view, MotionEvent motionEvent) {
			if (AUTO_HIDE) {
				delayedHide(AUTO_HIDE_DELAY_MILLIS);
			}
			return false;
		}
	};

	Handler mHideHandler = new Handler();
	Runnable mHideRunnable = new Runnable() {
		@Override
		public void run() {
			mSystemUiHider.hide();
		}
	};

	/**
	 * Schedules a call to hide() in [delay] milliseconds, canceling any
	 * previously scheduled calls.
	 */
	private void delayedHide(int delayMillis) {
		mHideHandler.removeCallbacks(mHideRunnable);
		mHideHandler.postDelayed(mHideRunnable, delayMillis);
	}

}
