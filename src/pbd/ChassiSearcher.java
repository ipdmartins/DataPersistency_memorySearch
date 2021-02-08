package pbd;

import java.io.IOException;
import java.io.RandomAccessFile;

public class ChassiSearcher {
	// ESTE BLOCO CONTÉM REGISTROS DE 249 BYTES
    private String chassi;//60 bytes
    private RandomAccessFile file;
    private ComparatorChassi chassiComparator;
    private String name;//90 bytes
    
	public ChassiSearcher(RandomAccessFile arquivo) {
		this.file = arquivo;
		this.chassi = "";
		this.name = "";
		this.chassiComparator = new ComparatorChassi();
	}
    
	//Recebe posicao de memoria, faz busca binaria por chassi,encontrando, retorna posicao de memoria
    public long searcher(long L5, int positionNumberReceived) {
    	long chassiPosition2 = L5;
    	int chassiInPositionMemory = 0;

        try {
            if (chassiPosition2 < file.length()) {//verifica se a posicao2 nao excede o tamanho de memoria
                if (positionNumberReceived == 1) {
                    file.seek(chassiPosition2);//aponta p/ posicao de memoria
                    System.out.println("Buscando chassi na posicao de memoria: " + file.getFilePointer() + "\n");
                    String chassi2 = ""; // variavel auxiliar para contruir o nome
                    for (int j = 0; j < 30; j++) {
                        chassi2 += file.readChar(); //le os chars do arquivo e guarda na String
                    }
                    System.out.println("Comparando chassi: " + chassi + " |chassi: " + chassi2 + "\n");
                    chassiInPositionMemory = chassiComparator.compare(chassi.trim(), chassi2.trim());//compara o chassi que o usuario indicou com o chassi que esta na posicao de memoria

                    System.out.println("Comparador retornou numero: " + chassiInPositionMemory + "\n");
                    if (chassiInPositionMemory == 0) {
                        System.out.println("chassi encontrado em memoria");
                        return (chassiPosition2);//retorna a posicao de memoria que esta o chassi
                    } else if (chassiInPositionMemory < 0) {//novo chassi menor que o atual
                        chassiPosition2 = chassiPosition2 + 232;//posiciona busca no filho esquerdo
                        file.seek(chassiPosition2);
                        System.out.println("Verificando filho esquerdo na posicao: " + file.getFilePointer() + "\n");
                        file.seek(chassiPosition2);
                        long childTree = file.readLong();
                        if (childTree == -1) {//verifica se o filho é nulo
                            System.out.println("retornando posicao filho esquerdo [-1]" + "\n");
                            return -1;
                        }
                        return searcher(childTree, 1);

                    } else if (chassiInPositionMemory > 0) {//novo chassi menor que o atual
                        chassiPosition2 = chassiPosition2 + 240;//posiciona busca no filho esquerdo
                        file.seek(chassiPosition2);
                        System.out.println("Verificando filho direito na posicao: " + file.getFilePointer() + "\n");
                        file.seek(chassiPosition2);
                        long aux2 = file.readLong();
                        if (aux2 == -1) {//verifica se o filho é nulo
                            System.out.println("retornando posicao filho direito [-1]" + "\n");
                            return -1;
                        }
                        return searcher(aux2, 1);
                    }
                } else if (positionNumberReceived == 2) {
                    file.seek(chassiPosition2 + 60);//aponta p/ posicao de memoria
                    System.out.println("Buscando nome na posicao: " + file.getFilePointer() + "\n");
                    String nome2 = "";
                    for (int i = 0; i < 45; i++) {
                        nome2 += file.readChar();
                    }
                    System.out.println("Comparando nome: " + name + " |nome: " + nome2 + "\n");
                    chassiInPositionMemory = chassiComparator.compare(name.trim(), nome2.trim());//trim elimina espaços entre palavras
                    System.out.println("Comparador retornou numero: " + chassiInPositionMemory + "\n");

                    if (chassiInPositionMemory == 0) {
                        System.out.println("nome encontrado em memoria");
                        return (chassiPosition2);//retorna a posicao de memoria que esta o chassi
                    } else {
                        chassiPosition2 = chassiPosition2 + 249;
                        return searcher(chassiPosition2, 2);
                    }
                }
            } else {
                System.out.println("Posicao excede o tamanho de memoria registrada");
            }
        } catch (IOException e) {
        	System.err.println("Erro no Chassi Searcher: " + e);
        }
        return (-1);
    }

}
