package com.letrinhas05;

import java.util.List;

import com.letrinhas05.BaseDados.LetrinhasDB;
import com.letrinhas05.ClassesObjs.CorrecaoTeste;
import com.letrinhas05.ClassesObjs.CorrecaoTesteLeitura;
import com.letrinhas05.ClassesObjs.Teste;
import com.letrinhas05.util.SystemUiHider;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.os.Build;

public class ResumoSubmissoes extends Activity {
	int testeID, alunoID;
	Button continuar;

	/**
	 * Whether or not the system UI should be auto-hidden after
	 * {@link #AUTO_HIDE_DELAY_MILLIS} milliseconds.
	 */
	private static final boolean AUTO_HIDE = true;
	/**
	 * If {@link #AUTO_HIDE} is set, the number of milliseconds to wait after
	 * user interaction before hiding the system UI.
	 */
	private static final int AUTO_HIDE_DELAY_MILLIS = 2000;
	/*********************************************************************
	 * The flags to pass to {@link SystemUiHider#getInstance}.
	 */
	private static final int HIDER_FLAGS = SystemUiHider.FLAG_HIDE_NAVIGATION;
	protected static final int PARADO = 0;
	/**
	 * The instance of the {@link SystemUiHider} for this activity.
	 */
	private SystemUiHider mSystemUiHider;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.resumo_submissoes);

		/* new line faz a rotacao do ecran 180 graus
		int currentOrientation = getResources().getConfiguration().orientation;
		if (currentOrientation == Configuration.ORIENTATION_LANDSCAPE) {
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
		} else {
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);
		}

		// / esconder o title************************************************+
		final View contentView = findViewById(R.id.resSub);

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

		// buscar os parametros */
		Bundle b = getIntent().getExtras();
		inicia(b);

	}

	/**
	 * m�todo para iniciar os componetes, que dependem do conteudo passado por
	 * parametros (extras)
	 * 
	 * @param b
	 *            Bundle, cont�m informa��o da activity anterior
	 */
	@SuppressLint("NewApi")
	public void inicia(Bundle b) {
		//
		testeID = b.getInt("IDTeste");
		alunoID = b.getInt("IDAluno");

		/** Consultar a BD para preencher o conteudo.... */
		LetrinhasDB bd = new LetrinhasDB(this);
		Teste teste = bd.getTesteById(testeID);

		// *** POR FAVOR TESTEM ISTO SF //////////////////////////////////oBRIGADO <3

		List<CorrecaoTesteLeitura> crt = bd.getAllCorrecaoTesteLeitura_ByIDaluno_TestID(alunoID, testeID);

		int j = 0;
		// procuro as que correspondem
		for (int i = 0; i < crt.size(); i++) {
			if (crt.get(i).getIdEstudante() == alunoID
					&& crt.get(i).getTestId() == testeID) {
				j++;
			}
		}

		// construo um array auxiliar
		CorrecaoTesteLeitura[] crtAux = new CorrecaoTesteLeitura[j];
		// copio a informa��o necess�ria
		j = 0;
		/*for (int i = 0; i < crt.size(); i++) {
			if (crt.get(i).getIdEstudante() == alunoID
					&& crt.get(i).getTestId() == testeID) {
				crtAux[j] = (CorrecaoTesteLeitura) crt.get(i);
				j++;
			}
		}
		// ****************************************************************/

		// Painel Dinamico
		// objetos do XML
		LinearLayout ll = (LinearLayout) findViewById(R.id.llResumo);
		Button btOriginal = (Button) findViewById(R.id.rsBtnOriginal);
		// remove o botao original do layerlayout
		ll.removeView(btOriginal);

		// Contruir os botoes
		for (int i = 0; i < crt.size(); i++){//crtAux.length; i++) {
			// criar o botao
			Button btIn = new Button(this);
			// copiar os parametros de layout
			btIn.setLayoutParams(btOriginal.getLayoutParams());
			// copiar a imagem do botao original
			btIn.setCompoundDrawables(null, null,
					btOriginal.getCompoundDrawablesRelative()[2], null);
			btIn.setText("" + crt.get(i).getDataExecucao());//crtAux[i].getDataExecucao());
			//final String audioUrl = crtAux[i].getAudiourl();

			// o que o bot�o faz
			btIn.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
				//	play(audioUrl);
				}

			});

			ll.addView(btIn);
		}

		TextView txt = ((TextView) findViewById(R.id.rsTituloTeste));
		txt.setText(teste.getTitulo());

		continuar = (Button) findViewById(R.id.rsAvancar);
		continuar.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				finish();
			}
		});

	}

	private void play(String audioUrl) {
		LinearLayout ll = (LinearLayout) findViewById(R.id.llResumo);
		ll.setEnabled(false);
		continuar.setEnabled(false);
		final LinearLayout llF = ll;
		final Button btt = continuar;
		final Handler play_handler;
		try {

			final MediaPlayer reprodutor = new MediaPlayer();
			reprodutor.setDataSource(audioUrl);
			reprodutor.prepare();
			reprodutor.start();
			Toast.makeText(getApplicationContext(), "A reproduzir.",
					Toast.LENGTH_SHORT).show();

			// espetar aqui uma thread, para quando isto pare
			// abilitar novamente a vista
			
				play_handler = new Handler() {
				public void handleMessage(Message msg) {
					switch (msg.what) {
					case PARADO:
						llF.setEnabled(true);
						btt.setEnabled(true);
						try {
							reprodutor.stop();
							reprodutor.release();
							Toast.makeText(getApplicationContext(),
									"Fim da reprodu��o.", Toast.LENGTH_SHORT)
									.show();
						} catch (Exception ex) {
						}
						break;
					default:
						break;
					}
				}
			};

			new Thread(new Runnable() {
				public void run() {
					while (reprodutor.isPlaying())
						;
					Message msg = new Message();
					msg.what = PARADO;
					play_handler.sendMessage(msg);
				}
			}).start();

		} catch (Exception ex) {
			Toast.makeText(getApplicationContext(),
					"Erro na reprodu��o.\n" + ex.getMessage(),
					Toast.LENGTH_SHORT).show();
		}

	}

	/*
	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);

		// Trigger the initial hide() shortly after the activity has been
		// created, to briefly hint to the user that UI controls
		// are available.
		delayedHide(2000);
	}

	/**
	 * Touch listener to use for in-layout UI controls to delay hiding the
	 * system UI. This is to prevent the jarring behavior of controls going away
	 * while interacting with activity UI.
	 */
	/*
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
	 *//*
	private void delayedHide(int delayMillis) {
		mHideHandler.removeCallbacks(mHideRunnable);
		mHideHandler.postDelayed(mHideRunnable, delayMillis);
	}*/

}
