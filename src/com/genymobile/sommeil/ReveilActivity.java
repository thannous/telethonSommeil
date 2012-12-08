package com.genymobile.sommeil;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Calendar;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Typeface;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.text.format.Time;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DigitalClock;
import android.widget.TimePicker;
import android.widget.Toast;

public class ReveilActivity extends Activity implements OnTimeSetListener {
	static final int ALARM_ID = 1234567;
	static Alarm alarm;
	int savetype;
	public static final int CODE_RETOUR = 0;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// Chargement des informations du reveil
		charger();

		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		
		DigitalClock digitalClock = (DigitalClock) findViewById(R.id.digitalClock);
		Typeface font = Typeface.createFromAsset(getAssets(), "SueEllenFrancisco.ttf");
		
		
		Button selectSonnerie = (Button) findViewById(R.id.selectSonnerie);
		selectSonnerie.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(
						RingtoneManager.ACTION_RINGTONE_PICKER);
				intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TYPE,
						RingtoneManager.TYPE_ALL);
				intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TITLE,
						"Select sonnerie");
				startActivityForResult(intent, CODE_RETOUR);
			}
		});

		// Affichage
		affichage();

		// Planification
		planifierAlarm();

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		// Vérification du code de retour
		if (requestCode == CODE_RETOUR) {
			// Vérifie que le résultat est OK
			if (resultCode == RESULT_OK) {
				
				 Uri uri = data.getParcelableExtra(RingtoneManager.EXTRA_RINGTONE_PICKED_URI);
				 // stocker uri
				 
				 SharedPreferences sharedPrefs = this.getSharedPreferences("TEST", MODE_PRIVATE);
				 Editor editor = sharedPrefs.edit();
				 editor.putString("ring", uri.toString());
				 editor.commit();
				 
				 Log.d("Alarm", "SETTING Ring: "+uri.toString());
			        
			        /*
				 if (uri != null) {
					 String ringtone = uri.getPath();
					 SoundPool ring =  new SoundPool(2, AudioManager.STREAM_RING, 1);
					 int soundID = ring.load(ringtone, 1);
			
				 String ringTonePath = uri.toString();
					Toast.makeText(this, "Votre nom est : " + ringTonePath,
							Toast.LENGTH_SHORT).show();
					
					saveas(soundID,uri);
				 }
				*/
				// Si l'activité est annulé
			} else if (resultCode == RESULT_CANCELED) {
				// On affiche que l'opération est annulée
				Toast.makeText(this, "Opération annulé", Toast.LENGTH_SHORT)
						.show();
			}
		}
	}

	public boolean saveas(int soundID, Uri uri) {
		byte[] buffer = null;
//		InputStream fIn = getBaseContext().getResources().openRawResource(soundID);
//		int size = 0;
//		try {
//			size = fIn.available();
//			buffer = new byte[size];
//			fIn.read(buffer);
//			fIn.close();
//		} catch (IOException e) {
//			Log.e("error", e.toString());
//			return false;
//		}

		String path = Environment.getExternalStorageDirectory().getPath()
				+ "/media/audio/ringtones/";
		String filename = "examplefile" + ".ogg";
//
//		boolean exists = (new File(path)).exists();
//		if (!exists) {
//			new File(path).mkdirs();
//		}
//
//		FileOutputStream save;
//		try {
//			save = new FileOutputStream(path + filename);
//			save.write(buffer);
//			save.flush();
//			save.close();
//		} catch (FileNotFoundException e) {
//			// TODO Auto-generated catch block
//			return false;
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			return false;
//		}
		sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,
				Uri.parse("file://" + path + filename)));
//
//		File k = new File(path, filename);
//
//		ContentValues values = new ContentValues();
//		values.put(MediaStore.MediaColumns.DATA, k.getAbsolutePath());
//		values.put(MediaStore.MediaColumns.TITLE, "exampletitle");
//		values.put(MediaStore.MediaColumns.MIME_TYPE, "audio/ogg");
//		values.put(MediaStore.Audio.Media.ARTIST, "cssounds ");
//		values.put(MediaStore.Audio.Media.IS_RINGTONE, true);
//		values.put(MediaStore.Audio.Media.IS_NOTIFICATION, true);
//		values.put(MediaStore.Audio.Media.IS_ALARM, true);
//		values.put(MediaStore.Audio.Media.IS_MUSIC, false);
//
//		// Insert it into the database
//		Uri newUri = this.getContentResolver()
//				.insert(MediaStore.Audio.Media.getContentUriForPath(k
//						.getAbsolutePath()), values);

		RingtoneManager.setActualDefaultRingtoneUri(this, RingtoneManager.TYPE_RINGTONE, uri);

		return true;
	}

	private void affichage() {

		String heureReveil = "";
		heureReveil += alarm.getHeure().hour > 10 ? alarm.getHeure().hour : "0"
				+ alarm.getHeure().hour;
		heureReveil += ":";
		heureReveil += alarm.getHeure().minute > 10 ? alarm.getHeure().minute
				: "0" + alarm.getHeure().minute;
		CheckBox ck_alarm = (CheckBox) findViewById(R.id.heure);
		ck_alarm.setText(heureReveil);
		ck_alarm.setChecked(alarm.isActive());

	}

	/*
	 * changeHeure se déclenche automatiquement au click sur l'heure ou la
	 * CheckBox. Active ou désactive le reveil. Affiche un dialog pour choisir
	 * l'heure de reveil
	 */
	public void changeHeure(View target) {
		CheckBox ck_alarm = (CheckBox) findViewById(R.id.heure);

		// choisir l'heure.
		if (ck_alarm.isChecked()) {
			TimePickerDialog dialog = new TimePickerDialog(this, this,
					alarm.getHeure().hour, alarm.getHeure().minute, true);
			dialog.show();
		}

		// On replanifie l'alarme.
		planifierAlarm();
	}

	/*
	 * Chargement des informations du reveil.
	 */
	public void charger() {
		alarm = null;
		try {
			ObjectInputStream alarmOIS = new ObjectInputStream(
					openFileInput("alarm.serial"));
			alarm = (Alarm) alarmOIS.readObject();
			alarmOIS.close();
		} catch (FileNotFoundException fnfe) {
			alarm = new Alarm();
			alarm.setActive(true);
			Time t = new Time();
			t.hour = 7;
			t.minute = 30;
			alarm.setHeure(t);
		} catch (IOException ioe) {
			ioe.printStackTrace();
		} catch (ClassNotFoundException cnfe) {
			cnfe.printStackTrace();
		}
	}

	/*
	 * Sauvegarde des informations du reveil
	 */
	public void sauver() {
		try {
			ObjectOutputStream alarmOOS = new ObjectOutputStream(
					openFileOutput("alarm.serial", MODE_WORLD_WRITEABLE));
			alarmOOS.writeObject(alarm);
			alarmOOS.flush();
			alarmOOS.close();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}

	/*
	 * Est activé après avoir valider l'heure du reveil.
	 */
	@Override
	public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
		Time t = new Time();
		t.hour = hourOfDay;
		t.minute = minute;
		alarm.setHeure(t);
		affichage();
		planifierAlarm();
	}

	/*
	 * Fonction planification du reveil
	 */
	private void planifierAlarm() {
		// Récupération de l'instance du service AlarmManager.
		AlarmManager am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

		Intent intent = new Intent(this, AlarmReceiver.class);
		intent.putExtra("vibrationPatern", new long[] { 200, 300 });

		PendingIntent pendingintent = PendingIntent.getBroadcast(this,
				ALARM_ID, intent, PendingIntent.FLAG_CANCEL_CURRENT);

		// On annule l'alarm pour replanifier
		am.cancel(pendingintent);

		// calcul le temps qui nous sépare du prochain reveil.
		Calendar reveil = Calendar.getInstance();
		reveil.set(Calendar.HOUR_OF_DAY, alarm.getHeure().hour);
		reveil.set(Calendar.MINUTE, alarm.getHeure().minute);
		if (reveil.compareTo(Calendar.getInstance()) == -1)
			reveil.add(Calendar.DAY_OF_YEAR, 1);

		Calendar cal = Calendar.getInstance();
		reveil.set(Calendar.SECOND, 0);
		cal.set(Calendar.SECOND, 0);
		long diff = reveil.getTimeInMillis() - cal.getTimeInMillis();

		// On ajoute le reveil au service de l'AlarmManager
		am.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis() + diff,
				pendingintent);
		Toast.makeText(
				this,
				"Alarme programmé le " + reveil.get(Calendar.DAY_OF_MONTH)
						+ " à " + reveil.get(Calendar.HOUR_OF_DAY) + ":"
						+ +reveil.get(Calendar.MINUTE), Toast.LENGTH_SHORT)
				.show();
	}

}