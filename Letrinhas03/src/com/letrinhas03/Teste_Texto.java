package com.letrinhas03;

import com.letrinhas03.util.SystemUiHider;

import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TableLayout;
import android.widget.TextView;
import android.os.Build;

public class Teste_Texto extends Activity {
	boolean modo=true, gravado;
	ImageButton record, play, voltar, cancelar, avancar;
	TextView vcl, frg, slb, rpt;
	int plvErradas, vacil, fragment, silabs, repeti;
	
	
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
		setContentView(R.layout.teste_texto);

		// esconder o title************************************************+
		final View contentView = findViewById(R.id.testTexto);

		// Set up an instance of SystemUiHider to control the system UI for
		// this activity.
		mSystemUiHider = SystemUiHider.getInstance(this, contentView,
				HIDER_FLAGS);
		mSystemUiHider.setup();
		mSystemUiHider.hide();

		if(modo){//est� em modo professor
			setCorreccao();
		}else{ //est� em modo aluno
			((TableLayout) findViewById(R.id.txtControlo)).setVisibility(View.INVISIBLE);
			
		}
		
		record = (ImageButton)findViewById(R.id.txtRecord);
		play = (ImageButton)findViewById(R.id.txtPlay);
		play.setVisibility(View.INVISIBLE);
		voltar = (ImageButton)findViewById(R.id.txtVoltar); 
		cancelar = (ImageButton)findViewById(R.id.txtCancel); 
		avancar = (ImageButton)findViewById(R.id.txtAvaliar);
		
		escutaBotoes();
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
				// voltar para pag inicial
				finish();
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
	}
	
	private void startGrava() {
		// TODO Auto-generated method stub
		
	}

	private void startPlay() {
		// TODO Auto-generated method stub
		
	}

	private void startAvalia() {
		if(modo){
			
		}
		
		finish();
		
	}
	
	/**
	 * Procedimento para ativar a selec��o das palavras erradas no texto
	 */
	private void setCorreccao(){
		//Painel de controlo: 
		ImageButton v1,v2,f1,f2,s1,s2,r1,r2;
		
		v1 = (ImageButton)findViewById(R.id.txtVacilMen);
		v2 = (ImageButton)findViewById(R.id.txtVacilMais);
		f1 = (ImageButton)findViewById(R.id.txtFragMen);
		f2 = (ImageButton)findViewById(R.id.txtFragMais);
		s1 = (ImageButton)findViewById(R.id.txtSilbMen);
		s2 = (ImageButton)findViewById(R.id.txtSilbMais);
		r1 = (ImageButton)findViewById(R.id.txtRepMen);
		r2 = (ImageButton)findViewById(R.id.txtRepMais);
		
		vcl = (TextView)findViewById(R.id.textView1);
		frg = (TextView)findViewById(R.id.TextView02); 
		slb = (TextView)findViewById(R.id.TextView03); 
		rpt = (TextView)findViewById(R.id.TextView06);
		
		vcl.setText(""+vacil);
		frg.setText(""+fragment);
		slb.setText(""+silabs);
		rpt.setText(""+repeti);
		
		//ativar os controlos
		v1.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {if(vacil!=0)vacil--;vcl.setText(""+vacil);}});
		v2.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {vacil++;vcl.setText(""+vacil);}});
		f1.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {if(fragment!=0)fragment--;frg.setText(""+fragment);}});
		f2.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {fragment++;frg.setText(""+fragment);}});
		s1.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {if(silabs!=0)silabs--;slb.setText(""+silabs);}});
		s2.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {silabs++;slb.setText(""+silabs);}});
		r1.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {if(repeti!=0)repeti--;rpt.setText(""+repeti);}});
		r2.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {repeti++;rpt.setText(""+repeti);}});
		
		
	}
	
	
	/**
	 * Procedimento para voltar a esconder o titulo caso este esteja activo
	 * 
	 * @author Thiago
	 */
	@Override
	public boolean hasWindowFocus() {
		// esconder o title
		final View contentView = findViewById(R.id.testTexto);
		// Set up an instance of SystemUiHider to control the system UI for
		// this activity.
		mSystemUiHider = SystemUiHider.getInstance(this, contentView,
				HIDER_FLAGS);
		mSystemUiHider.setup();
		mSystemUiHider.hide();
		return true;
	}

}
