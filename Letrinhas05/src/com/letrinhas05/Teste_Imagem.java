package com.letrinhas05;

import java.io.File;
import java.util.List;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Chronometer;
import android.widget.ImageButton;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.letrinhas05.BaseDados.LetrinhasDB;
import com.letrinhas05.ClassesObjs.TesteMultimedia;
import com.letrinhas05.R;
import com.letrinhas05.util.SystemUiHider;
import com.letrinhas05.util.Teste;

public class Teste_Imagem extends Activity {

			// flags para verificar os diversos estados do teste
			boolean modo, gravado, recording, playing;
			// objetos
			ImageButton record, play, voltar, cancelar, avancar, hipotese1, hipotese2, hipotese3, exeTeste;
			int ntesteMultimedia, idTesteAtual;
			Chronometer chrono;
			private MediaRecorder gravador;
			private MediaPlayer reprodutor = new MediaPlayer();
			private String endereco;
			Teste[] lista;
			TextView auxiliar;
			LetrinhasDB db;
			TesteMultimedia testeMultimedia;
			Integer[] image;
			int[] testesID, correctOption,iDs;
			String[] conteudoQuestao;
			String[] opcao1;
			String[] opcao2;
			String[] opcao3;
			String[] Nomes;
			String[] fotoNome;
			
			
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
					setContentView(R.layout.teste_imagem);

					//new line faz a rota��o do ecr�n 180 graus
					int currentOrientation = getResources().getConfiguration().orientation;
					if (currentOrientation == Configuration.ORIENTATION_LANDSCAPE) {
					setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
					}else {
					setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);
					}
					// / esconder o title************************************************+
					final View contentView = findViewById(R.id.testPalavras);
					// Set up an instance of SystemUiHider to control the system UI for
					// this activity.
					mSystemUiHider = SystemUiHider.getInstance(this, contentView,HIDER_FLAGS);
					mSystemUiHider.setup();
					mSystemUiHider.setOnVisibilityChangeListener(new SystemUiHider.OnVisibilityChangeListener() {
						// Cached values.
						int mShortAnimTime;
								@Override
								@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
								public void onVisibilityChange(boolean visible) {
									if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
										if (mShortAnimTime == 0) {
											mShortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);
										}
									}
									if (visible && AUTO_HIDE) {
										// Schedule a hide().
										delayedHide(AUTO_HIDE_DELAY_MILLIS);
									}
								}
							});
					// buscar os parametros
					Bundle b = getIntent().getExtras();
					inicia(b);
					
					endereco = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + b.getString("Professor") + "/" + b.getString("Aluno") + "/" + lista[0].getTitulo() + ".3gpp";
					// descontar este teste da lista.
					Teste[] aux = new Teste[lista.length - 1];
					for (int i = 1; i < lista.length; i++) {
						aux[i - 1] = lista[i];
					}
					lista = aux;
					if (!modo) {// est� em modo professor
						((TableLayout) findViewById(R.id.txtControlo)).setVisibility(View.INVISIBLE);
					} 
					record = (ImageButton) findViewById(R.id.txtRecord);
					play = (ImageButton) findViewById(R.id.txtPlay);
					play.setVisibility(View.INVISIBLE);
					voltar = (ImageButton) findViewById(R.id.txtVoltar);
					cancelar = (ImageButton) findViewById(R.id.txtCancel);
					avancar = (ImageButton) findViewById(R.id.txtAvaliar);
					hipotese1 = (ImageButton) findViewById(R.id.hipotese1);
					hipotese2 = (ImageButton) findViewById(R.id.hipotese2);
					hipotese3 = (ImageButton) findViewById(R.id.hipotese3);
					//exeTeste = (ImageButton) findViewById(R.id.exeTeste);
					escutaBotoes();
					//makeTeste();
				}

				/**
				 * m�todo para iniciar os componetes, que dependem do conteudo passado
				 * por parametros (extras)
				 * 
				 * @param b Bundle, cont�m informa��o da activity anterior
				 */
				public void inicia (Bundle b){
					// Compor novamente e lista de testes
					testesID = b.getIntArray("ListaID");
					// String's - Escola, Professor, fotoProf, Turma, Aluno, fotoAluno
					Nomes = b.getStringArray("Nomes");
					// int's - idEscola, idProfessor, idTurma, idAluno
					iDs = b.getIntArray("IDs");

					/** Consultar a BD para preencher o conte�do.... */
					LetrinhasDB bd = new LetrinhasDB(this);
					testeMultimedia =  (TesteMultimedia) bd.getTesteById(testesID[0]);
					Log.d("Debug-Iniciar(b)", "testesID->"+String.valueOf(testesID[0])+" teste->"+testeMultimedia.getTexto());
					Log.d("Debug-getTitulo()", testeMultimedia.getTitulo());
					String[] yo = testeMultimedia.getConteudoQuestao().split("[ ]");
					String opcao1[] = testeMultimedia.getOpcao1().split("[ ]");
					String opcao2[] = testeMultimedia.getOpcao2().split("[ ]");
					String opcao3[] = testeMultimedia.getOpcao3().split("[ ]");
					
					String image1 = Environment.getExternalStorageDirectory().getAbsolutePath() + "/School-Data/MultimediaTest/" + opcao1;
					String image2 = Environment.getExternalStorageDirectory().getAbsolutePath() + "/School-Data/MultimediaTest/" + opcao2;
					String image3 = Environment.getExternalStorageDirectory().getAbsolutePath() + "/School-Data/MultimediaTest/" + opcao3;
 					
					Bitmap bitmap1 = BitmapFactory.decodeFile(image1);
					Bitmap bitmap2 = BitmapFactory.decodeFile(image2);
					Bitmap bitmap3 = BitmapFactory.decodeFile(image3);
										
					
					String yo2 = "";
					((TextView) findViewById(R.id.textCabecalho)).setText(testeMultimedia.getTitulo());
					for(int i = 0; i<yo.length;i++){
						yo2 += yo[i]+"\n";
					}
					((TextView) findViewById(R.id.ConteudoQuest)).setText(yo2);
					
					hipotese1.setImageBitmap(bitmap1);
					hipotese2.setImageBitmap(bitmap2);
					hipotese3.setImageBitmap(bitmap3);

					// **********************************************************************************************

					idTesteAtual = testesID[0];
					// descontar este teste da lista.
					int[] aux = new int[testesID.length - 1];
					for (int i = 1; i < testesID.length; i++) {
						aux[i - 1] = testesID[i];
					}
					testesID = aux;
				}
				
				
				/**private void makeTeste() {
					
					
					Bundle b = getIntent().getExtras();
					
					// Cria o objecto da base de dados
					db = new LetrinhasDB(this);
					testeMultimedia = db.getAllTesteMultimedia();
					ntesteMultimedia = testeMultimedia.size();
					opcao1 = new String [ntesteMultimedia];
					opcao2 = new String [ntesteMultimedia];
					opcao3 = new String [ntesteMultimedia];
					correctOption = new int [ntesteMultimedia];
					
					
					
					for(int i = 0; i < ntesteMultimedia; i++){
						conteudoQuestao[i] = testeMultimedia.get(i).getConteudoQuestao();
						opcao1[i] = testeMultimedia.get(i).getOpcao1();
						opcao2[i] = testeMultimedia.get(i).getOpcao2();
						opcao3[i] = testeMultimedia.get(i).getOpcao3();
						correctOption[i] = testeMultimedia.get(i).getCorrectOption();
						
					}
					
				}*/

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

				public void setUp() {
					gravador = new MediaRecorder();
					gravador.setAudioSource(MediaRecorder.AudioSource.MIC);
					gravador.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
					gravador.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
					// construir as pastas caso necess�rio
					File file = new File(endereco);
					if (file.getParent() != null && !file.getParentFile().exists()) {
						file.getParentFile().mkdirs();
					}
					gravador.setOutputFile(endereco);
				}

				private void escutaBotoes() {
					record.setOnClickListener(new View.OnClickListener() {
						@Override
						public void onClick(View view) {
							startGrava();
						}

					});
					play.setOnClickListener(new View.OnClickListener() {
						@Override
						public void onClick(View view) {
							startPlay();
						}
					});
					cancelar.setOnClickListener(new View.OnClickListener() {
						@Override
						public void onClick(View view) {
							// salta a avalia��o e vai para o pr�ximo teste descurando a
							// grava��o gerada
							File file = new File(endereco);
							if (file.exists()) {
								file.delete();
							}
							finaliza();
						}
					});
					avancar.setOnClickListener(new View.OnClickListener() {
						@Override
						public void onClick(View view) {
							// voltar para pag inicial
							startAvalia();
						}
					});
					voltar.setOnClickListener(new View.OnClickListener() {
						@Override
						public void onClick(View view) {
							// voltar para pag inicial
							finish();
						}
					});
					hipotese1.setOnClickListener(new View.OnClickListener() {	
						@Override
						public void onClick(View v) {
							
						}
					});
					hipotese2.setOnClickListener(new View.OnClickListener() {	
						@Override
						public void onClick(View v) {
							
						}
					});
					hipotese3.setOnClickListener(new View.OnClickListener() {	
						@Override
						public void onClick(View v) {
							
						}
					});
				}
				
				
				int minuto, segundo;
				/**
				 * Serve para come�ar ou parar o recording do audio
				 * 
				 * @author D�rio Jorge
				 */
				@SuppressLint("HandlerLeak")
				private void startGrava() {
					if (!recording) {
						record.setImageResource(R.drawable.stop);
						play.setVisibility(View.INVISIBLE);
						cancelar.setVisibility(View.INVISIBLE);
						voltar.setVisibility(View.INVISIBLE);
						avancar.setVisibility(View.INVISIBLE);
						recording = true;
						try {
							setUp();
							gravador.prepare();
							gravador.start();
							Toast.makeText(getApplicationContext(), "A gravar.",Toast.LENGTH_SHORT).show();
							// O cronometro n�o funciona assim t�o bem no seu modo
							// original...
							chrono = (Chronometer) findViewById(R.id.cromTxt);
							// handler para controlar a GUI do android e a thread seguinte
							play_handler = new Handler() {
								@SuppressLint("HandlerLeak")
								public void handleMessage(Message msg) {
									switch (msg.what) {
									case PARADO:
										String m,
										s;
										if (minuto < 10) {
											m = "0" + minuto + ":";
										} else {
											m = minuto + ":";
										}
										if (segundo < 10) {
											s = "0" + segundo;
										} else {
											s = "" + segundo;
										}
										chrono.setText(m + s);
										break;
									default:
										break;
									}
								}
							};
							// pequena thread, para interagir com o cronometro
							new Thread(new Runnable() {
								public void run() {
									minuto = 0;
									segundo = 0;
									while (recording) {
										Message msg = new Message();
										msg.what = PARADO;
										play_handler.sendMessage(msg);

										try {
											Thread.sleep(1000);
										} catch (InterruptedException e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										}
										segundo++;
										if (segundo == 60) {
											minuto++;
											segundo = 0;
										}
									}
								}
							}).start();
						} catch (Exception e) {
							Toast.makeText(getApplicationContext(),"Erro na grava��o.\n" + e.getMessage(),Toast.LENGTH_SHORT).show();
						}
					} else {
						record.setImageResource(R.drawable.record);
						play.setVisibility(View.VISIBLE);
						cancelar.setVisibility(View.VISIBLE);
						voltar.setVisibility(View.VISIBLE);
						avancar.setVisibility(View.VISIBLE);
						recording = false;
						try {
							gravador.stop();
							gravador.release();
							Toast.makeText(getApplicationContext(),"Grava��o efetuada com sucesso!", Toast.LENGTH_SHORT).show();
							Toast.makeText(getApplicationContext(),"Tempo de leitura: " + minuto + ":" + segundo,Toast.LENGTH_LONG).show();
						} catch (Exception e) {
							Toast.makeText(getApplicationContext(),"Erro na grava��o.\n" + e.getMessage(),Toast.LENGTH_SHORT).show();
						}
					}
				}
				private final int PARADO = 2;
				private Handler play_handler;
				/**
				 * serve para a aplica��o reproduzir ou parar o som
				 * 
				 * @author D�rio Jorge
				 */
				@SuppressLint("HandlerLeak")
				private void startPlay() {
					if (!playing) {
						play.setImageResource(R.drawable.play_on);
						record.setVisibility(View.INVISIBLE);
						playing = true;
						try {
							reprodutor = new MediaPlayer();
							reprodutor.setDataSource(endereco);
							reprodutor.prepare();
							reprodutor.start();
							Toast.makeText(getApplicationContext(), "A reproduzir.",Toast.LENGTH_SHORT).show();
							// espetar aqui uma thread, para caso isto pare
							// handler para controlara a GUI do androi e a thread seguinte
							play_handler = new Handler() {
								public void handleMessage(Message msg) {
									switch (msg.what) {
									case PARADO:
										play.setImageResource(R.drawable.palyoff);
										record.setVisibility(View.VISIBLE);
										playing = false;
										try {
											reprodutor.stop();
											reprodutor.release();
											Toast.makeText(getApplicationContext(),"Fim da reprodu��o.",Toast.LENGTH_SHORT).show();
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
									while (reprodutor.isPlaying());
									Message msg = new Message();
									msg.what = PARADO;
									play_handler.sendMessage(msg);
								}
							}).start();
						} catch (Exception ex) {
							Toast.makeText(getApplicationContext(),"Erro na reprodu��o.\n" + ex.getMessage(),Toast.LENGTH_SHORT).show();
						}
					} else {
						play.setImageResource(R.drawable.palyoff);
						record.setVisibility(View.VISIBLE);
						playing = false;
						try {
							reprodutor.stop();
							reprodutor.release();

							Toast.makeText(getApplicationContext(),"Reprodu��o interrompida.", Toast.LENGTH_SHORT).show();
						} catch (Exception ex) {
							Toast.makeText(getApplicationContext(),"Erro na reprodu��o.\n" + ex.getMessage(),Toast.LENGTH_SHORT).show();
						}
					}
				}
				private void startAvalia() {
					if (modo) { // se est� em modo de professor
								// inicia a avalia��o
						File file = new File(endereco);
						if (file.exists()) { // se j� fez uma grava��o
							// uma pop-up ou activity para determinar o valor de
							// exprecividade da leitura
							// usar a classe Avalia��o para calcular os resultados.
							// avan�ar para o pr�ximo teste caso este exista.
							finaliza();
						} else {
							android.app.AlertDialog alerta;
							// Cria o gerador do AlertDialog
							AlertDialog.Builder builder = new AlertDialog.Builder(this);
							// define o titulo
							builder.setTitle("Letrinhas 03");
							// define a mensagem
							builder.setMessage(" Ainda n�o executou a grava��o da leitura!\n"+ " Fa�a-o antes de avaliar.");
							// define um bot�o como positivo
							builder.setPositiveButton("OK", null);
							// cria o AlertDialog
							alerta = builder.create();
							// Mostra
							alerta.show();
						}
					}
				}
		
				

				/**
				 * Prepara a finaliza��o da activity, descobrindo qual o pr�ximo teste a
				 * realizar Este m�todo dever� ser usado em todas as paginas de teste.
				 */
				private void finaliza() {
					if (lista.length != 0) {
						// Decompor o array de teste, para poder enviar por parametros
						int[] lstID = new int[lista.length];
						int[] lstTipo = new int[lista.length];
						String[] lstTitulo = new String[lista.length];
						for (int i = 0; i < lista.length; i++) {
							lstID[i] = lista[i].getID();
							lstTipo[i] = lista[i].getTipo();
							lstTitulo[i] = lista[i].getTitulo();
						}
						// enviar o parametro de modo
						Bundle wrap = new Bundle();
						wrap.putBoolean("Modo", modo);
						// teste, a depender das informa��es da BD
						// ***********************************************************+
						wrap.putString("Aluno", "EI3C-Tiago Fernandes");
						wrap.putString("Professor", "ESTT-Antonio Manso");
						wrap.putIntArray("ListaID", lstID);
						wrap.putIntArray("ListaTipo", lstTipo);
						wrap.putStringArray("ListaTitulo", lstTitulo);
						// identifico o tipo de teste
						switch (lista[0].getTipo()) {
						case 0:
							// lan�ar a nova activity do tipo texto,
							// iniciar a pagina 2 (escolher teste)
							Intent it = new Intent(getApplicationContext(),Teste_Texto_Aluno.class);
							it.putExtras(wrap);
							startActivity(it);
							break;
						case 1:// lan�ar a nova activity do tipo Palavras, e o seu conte�do
								//
							Intent ip = new Intent(getApplicationContext(),Teste_Imagem.class);
							ip.putExtras(wrap);
							startActivity(ip);
							break;
						case 2: // lan�ar a nova activity do tipo Poema, e o seu conte�do
							//
							Intent ipm = new Intent(getApplicationContext(),Teste_Poema_Aluno.class);
							ipm.putExtras(wrap);
							startActivity(ipm);
							break;
						case 3: // lan�ar a nova activity do tipo imagem, e o seu conte�do
							//
							Intent im = new Intent(getApplicationContext(),Teste_Palavras_Aluno.class);
							im.putExtras(wrap);
							 startActivity(im);
							break;
						default:
							Toast.makeText(getApplicationContext(), " - Tipo n�o defenido",Toast.LENGTH_SHORT).show();
							// retirar o teste errado e continuar
							int k = 0;
							Teste aux[] = new Teste[lista.length - 1];
							for (int i = 1; i < lista.length; i++) {
								aux[k] = lista[i];
								k++;
							}
							lista = aux;
							finaliza();
							break;
						}
					}
					finish();
				}

}
