package pbd;

import java.io.IOException;
import java.io.RandomAccessFile;

public class PositionSearcher {
	private RandomAccessFile file;
	private String chassi;
	private ComparatorChassi chassiComparator;
	
	public PositionSearcher(RandomAccessFile file, String chassi, ComparatorChassi chassiComparator) {
		this.file = file;
		this.chassi = "";
		this.chassiComparator = new ComparatorChassi();
	}

	//Recebe posicao de memoria, faz busca binaria por chassi, verifica se o filho é -1
    //encontrando, retorna a posicao de memoria para fazer a gravação
	public long positionSearcher(long L3) {
		
		//inicia busca do chassi na posicao zero
        long position = L3;
        try {
            file.seek(position);//aponta p/ posicao de memoria
            System.out.println("Arquivo na posição: " + file.getFilePointer() + "\n");
            String chassi1 = "";//variavel auxiliar para contruir o chassi
            for (int j = 0; j < 30; j++) {
                chassi1 += file.readChar(); //le os chars do arquivo e guarda na String
            }
            //compara o chassi que o usuario indicou com o chassi que esta na posicao de memoria
            int ret = chassiComparator.compare(chassi.trim(), chassi1.trim());
            System.out.println("Comparador de chassi retornou: " + ret + "\n");
            if (ret == 0) {
                return 0;
            } else if (ret < 0) {//novo chassi menor que o atual
                position = position + 232;//posiciona busca no filho esquerdo
                file.seek(position);
                System.out.println("Verifica filho esquerdo na posicao: " + file.getFilePointer() + "\n");
                Long aux1 = file.getFilePointer();
                long num = file.readLong();
                file.seek(position + 16);
                Boolean aux2 = file.readBoolean();

                if (num == -1) {
                    System.out.println("Filho esquerdo igual a: " + num + "\n");
                    file.seek(aux1);
                    file.writeLong(file.length());
                    file.seek(aux1);
                    num = file.readLong();
                    System.out.println("No filho esquerdo (-1) foi gravado posicao de memoria: " + num + "\n");
                    return (file.length());
                } else if (aux2 == true) {
                    System.out.println("Registro filho havia sido apagado, possivel"
                            + "gravar na mesma posicao de memoria: " + aux1 + "\n");
                    return (aux1);
                } else {
                    return positionSearcher(num);
                }
            } else if (ret > 0) {
                position = position + 240;//posiciona busca no filho direito
                file.seek(position);
                System.out.println("Verifica filho direito na posicao: " + file.getFilePointer() + "\n");
                Long aux3 = file.getFilePointer();
                long num2 = file.readLong();
                file.seek(position + 8);
                Boolean aux4 = file.readBoolean();

                if (num2 == -1) {
                    System.out.println("Filho direito igual a: " + num2 + "\n");
                    file.seek(aux3);
                    file.writeLong(file.length());
                    file.seek(aux3);
                    num2 = file.readLong();
                    System.out.println("No filho esquerdo (-1) foi gravado posicao de memoria: " + num2 + "\n");
                    return (file.length());
                } else if (aux4 == true) {
                    System.out.println("Registro filho direito havia sido apagado, possivel"
                            + "gravar na mesma posicao de memoria: " + aux3 + "\n");
                    return (aux3);
                } else {
                    return positionSearcher(num2);
                }
            }
        } catch (IOException e) {
            System.err.println("Erro no Position Searcher: " + e);
        }
        return -1;
	}

}
