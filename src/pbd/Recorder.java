package pbd;

import java.io.IOException;
import java.io.RandomAccessFile;

public class Recorder {
	private RandomAccessFile file;
	private double value;
	private String chassi;
	private String name;
	private String color;
	private int quantity;
	private long fileSize;
	private Printer printer;
	
	public Recorder() {
		this.value = 0; //8 bytes
		this.chassi = ""; //60 bytes
		this.name = ""; //90 bytes
		this.color = ""; //70 bytes
		this.quantity = 0; //4 bytes
		this.fileSize = 0; //codigo do Tiago 
		this.printer = new Printer();
	}



	//Grava novos atributos na posicao de memoria indicada
	public void grava(long L1) {
        try {
            file.seek(L1);//aponta p/ posicao de memoria
            System.out.println("Gravando arquivo na posição: " + file.getFilePointer() + "\n");

            file.writeChars(String.format("%1$30s", chassi));
            //grava nome formatado com tamanho de 45 caracteres    
            file.writeChars(String.format("%1$45s", name));
            //grava nome formatado com tamanho de 35 caracteres
            file.writeChars(String.format("%1$35s", color));
            file.writeDouble(value);//grava o valor
            file.writeInt(quantity);//grava a quantidade
            file.writeLong(-1);//grava ponteiro fim vetor para 'filho esquerda nulo'
            file.writeLong(-1);//grava ponteiro fim vetor para 'filho direita nulo'
            file.writeBoolean(false);

            fileSize = file.length(); //recupera o tamanho do arquivo
            System.out.println("Arquivo gravado e fechado com " + fileSize + " bytes." + "\n");
            System.out.println(printer.printer(L1));
            file.close(); //fecha o arquivo
        } catch (IOException ex) {
            System.exit(1);
        }
	}

}
