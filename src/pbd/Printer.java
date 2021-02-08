package pbd;

import java.io.IOException;
import java.io.RandomAccessFile;

public class Printer {
	private RandomAccessFile file;
	
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
            //System.out.print("{ " + "Chassi: " + auxS.trim());
            finale += auxS.trim();
            auxS = "";
            for (int i = 0; i < 45; i++) {
                auxS += file.readChar();
            }
            //System.out.print("| Nome: " + auxS.trim());
            finale += "| Nome: " + auxS.trim();
            auxS = "";
            for (int i = 0; i < 35; i++) {
                auxS += file.readChar();
            }
            //System.out.print("| Cor: " + auxS.trim());
            finale += "| Cor: " + auxS.trim();
            
            auxD = file.readDouble();
            //System.out.print("| Valor: " + auxD);
            finale += "| Valor: " + auxD;
            
            auxI = file.readInt();
            //System.out.print("| Quantidade: " + auxI);
            finale += "| Quantidade: " + auxI;
            
            auxL = file.readLong();
            //System.out.print("| Filho E: " + auxL);
            finale += "| Filho E: " + auxL;
            
            auxL = file.readLong();
            //System.out.print("| Filho D: " + auxL);
            finale += "| Filho D: " + auxL;
            
            auxB = file.readBoolean();
            //System.out.print("| Apagado?: " + auxB + "\n");
            finale += "| Apagado?: " + auxB;
        } catch (IOException ex) {
            System.err.println("Erro no Printer: " + ex);
        }
        System.out.println("");
        return finale;
    }

}
