package game;

import java.io.BufferedInputStream;
import java.io.InputStream;
import javax.sound.sampled.*;

public class Sound implements Runnable {

    // Atributos
    private String audioPath;
    private boolean loop, stop = false;

    //Contrutores
    public Sound(String audioPath, boolean loop) {
        this.audioPath = audioPath;
        this.loop = loop;
    }

    // Métodos específicos
    public void play () {
        Thread t = new Thread(this);
        t.start();
    }

    @Override
    public void run() {
        try {
            // Inicia a reprodução em loop infinito
            do{
                //read audio data from whatever source (file/classloader/etc.)
                InputStream audioSrc = getClass().getResourceAsStream(audioPath);
                //add buffer for mark/reset support
                assert audioSrc != null;
                InputStream bufferedIn = new BufferedInputStream(audioSrc);
                AudioInputStream audioStream = AudioSystem.getAudioInputStream(bufferedIn);

                // Obtém o formato de áudio do arquivo .wav
                AudioFormat audioFormat = audioStream.getFormat();

                // Cria um DataLine.Info para a linha de reprodução
                DataLine.Info info = new DataLine.Info(SourceDataLine.class, audioFormat);

                // Obtém a linha de reprodução do sistema
                SourceDataLine line = (SourceDataLine) AudioSystem.getLine(info);

                // Abre novamente a linha de reprodução
                line.open(audioFormat);

                // Inicia a reprodução
                line.start();

                // Cria um buffer para armazenar os dados do áudio
                byte[] buffer = new byte[4096];
                int bytesRead = 0;

                // Lê dados do áudio do InputStream e escreve na linha de reprodução
                while ((bytesRead = audioStream.read(buffer)) != -1) {
                    line.write(buffer, 0, bytesRead);
                    if(getStop()) {
                        while (getStop());
                        break;
                    }
                }

                // Encerra a reprodução
                line.drain();
                line.stop();
                line.close();
                audioStream.close();
            }while (loop);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Métodos de acesso
    public boolean isLoop() {
        return loop;
    }

    public void setLoop(boolean loop) {
        this.loop = loop;
    }

    public void setStop(boolean stop) {
        this.stop = stop;
    }

    public boolean getStop() {
        return stop;
    }

}