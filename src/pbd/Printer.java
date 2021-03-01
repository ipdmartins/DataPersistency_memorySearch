package pbd;

import java.io.IOException;
import java.io.RandomAccessFile;

public class Printer {
	private RandomAccessFile file;
	
	public Printer(RandomAccessFile arquivo) {
		this.file = arquivo;
	}
	
	//imprimi os dados registrados em memoria
    public String printer(long L2) {
        String finale = "{Chassi: ";
        try {
            file.seek(L2);
            int auxI = -1;
            String auxS = "";
            Double auxD = 0.0;
            boolean auxB = true;
            long auxL = -1;
            
            for (int i = 0; i < 30; i++) {
                auxS += file.readChar();
            }
            finale += auxS.trim();
            auxS = "";
            for (int i = 0; i < 45; i++) {
                auxS += file.readChar();
            }
            finale += "| Nome: " + auxS.trim();
            auxS = "";
            for (int i = 0; i < 35; i++) {
                auxS += file.readChar();
            }
            finale += "| Cor: " + auxS.trim();
            
            auxD = file.readDouble();
            finale += "| Valor: " + auxD;
            
            auxI = file.readInt();
            finale += "| Quantidade: " + auxI;
            
            auxL = file.readLong();
            finale += "| Filho E: " + auxL;
            
            auxL = file.readLong();
            finale += "| Filho D: " + auxL;
            
            auxB = file.readBoolean();
            finale += "| Apagado?: " + auxB;
        } catch (IOException ex) {
            System.err.println("Erro no Printer: " + ex);
        }
        System.out.println("");
        return finale;
    }

}
