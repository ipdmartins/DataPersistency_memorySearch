/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pbd;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Comparator;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

public class TesteCarroMemoria {

    // ESTE BLOCO CONTÉM REGISTROS DE 249 BYTES
    static String chassi = "";//60 bytes
    static String name = "";//90 bytes
    static String color = "";//70 bytes
    static double value = 0;//8 bytes
    static int quantity = 0;//4 bytes
    static long esquerda = -1;//filho da esquerda 8 bytes
    static long direita = -1;//filho da esquerda 8 bytes
    static Boolean isdead = false;//confere se o arquivo foi excluido logicamente 1 byte

    static int busca = 0;//registra opcao de busca do usuario, chassi ou nome;
    static int opcao = 0;//registra opcao do usuario no menu
    static Scanner imput = new Scanner(System.in);
    static ComparatorChassi chassiComparator = new ComparatorChassi();

    //MANIPULAÇÃO DE ARQUIVO
    static RandomAccessFile file;
    static long fileSize = 0;//codigo do Tiago    
    static String nomeArquivo = "texto.txt";//codigo do Tiago

    //Recebe posicao de memoria, faz busca binaria por chassi,encontrando, retorna posicao de memoria
    public static long buscador(long L5, int positionNumberReceived) {
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
                        return buscador(childTree, 1);

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
                        return buscador(aux2, 1);
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
                        return buscador(chassiPosition2, 2);
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

    //Recebe posicao de memoria, faz busca binaria por chassi, verifica se o filho é -1
    //encontrando, retorna a posicao de memoria para fazer a gravação
    public static long buscaPosicao(long L3) throws IOException {
        long posicao = L3;//inicia busca do chassi na posicao zero
        try {
            file.seek(posicao);//aponta p/ posicao de memoria
            System.out.println("Arquivo na posição: " + file.getFilePointer() + "\n");
            String chassi1 = "";//variavel auxiliar para contruir o chassi
            for (int j = 0; j < 30; j++) {
                chassi1 += file.readChar(); //le os chars do arquivo e guarda na String
            }
            int ret = chassiComparator.compare(chassi.trim(), chassi1.trim());//compara o chassi que o usuario indicou com o chassi que esta na posicao de memoria
            System.out.println("Comparador de chassi retornou: " + ret + "\n");
            if (ret == 0) {
                return 0;
            } else if (ret < 0) {//novo chassi menor que o atual
                posicao = posicao + 232;//posiciona busca no filho esquerdo
                file.seek(posicao);
                System.out.println("Verifica filho esquerdo na posicao: " + file.getFilePointer() + "\n");
                Long aux1 = file.getFilePointer();
                long num = file.readLong();
                file.seek(posicao + 16);
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
                    return buscaPosicao(num);
                }
            } else if (ret > 0) {
                posicao = posicao + 240;//posiciona busca no filho direito
                file.seek(posicao);
                System.out.println("Verifica filho direito na posicao: " + file.getFilePointer() + "\n");
                Long aux3 = file.getFilePointer();
                long num2 = file.readLong();
                file.seek(posicao + 8);
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
                    return buscaPosicao(num2);
                }
            }
        } catch (IOException e) {
            System.exit(1);
        }
        return -1;
    }

    //Grava novos atributos na posicao de memoria indicada
    public static void grava(long L1) {
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
            System.out.println(imprimir(L1));
            file.close(); //fecha o arquivo
        } catch (IOException ex) {
            System.exit(1);
        }
    }

    //imprimi os dados registrados em memoria
    public static String imprimir(long L2) {
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
            Logger.getLogger(TesteCarroMemoria.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("");
        return finale;
    }
    
    public static void printTree(long mem, int nivel){
        long right = 0;
        long left = 0;
        String level = "";
        
        try {
            file.seek(mem+232);
            left = file.readLong();
            file.seek(mem+240);
            right = file.readLong();
            
            if(mem == 0)
                System.out.println("RAIZ: " + imprimir(mem));
            
            for (int i = 0; i <= nivel; i++) 
                level +="-----|";
            
            if(right != -1){
                System.out.println(level + "Filho direito: " + imprimir(right));
                printTree(right, nivel+1);
            }
            if(left != -1){
                System.out.println(level + "Filho esquerdo: " +imprimir(left));
                printTree(left, nivel+1);
            }
        } catch (Exception e) {
            System.exit(1);
        }
        
    }

    public static void main(String[] args) {
        File arq = new File(nomeArquivo);//objeto para manipulacao fisica do arquivo
        arq.delete();

        try {
            while (opcao != 6) {
                System.out.println("");
                System.out.println("ESCOLHA UMA OPCAO: ");
                System.out.println("1. Adicionar");
                System.out.println("2. Remover");
                System.out.println("3. Alterar");
                System.out.println("4. Consultar");
                System.out.println("5. Imprimir arvore");
                System.out.println("6. Fechar\n");
                opcao = imput.nextInt();
                        
                file = new RandomAccessFile(arq, "rw");//Abre arquivo para leitura e gravacao
                fileSize = file.length();//recupera o tamanho do arquivo
                System.out.println("Arquivo aberto com tamanho atual de " + fileSize + " bytes\n");

                switch (opcao) {
                    case 1:
                        System.out.print("Informe o chassi: ");
                        chassi = imput.nextLine();
                        chassi = imput.nextLine();

                        System.out.print("Informe o nome: ");
                        name = imput.nextLine();

                        System.out.print("Informe a cor: ");
                        color = imput.nextLine();

                        System.out.print("Informe o valor: ");
                        value = imput.nextDouble();

                        System.out.print("Informe a quantidade: ");
                        quantity = imput.nextInt();

                        if (fileSize == 0) {//tamanho zero, basta gravar, nao precisa checagem
                            grava(0);
                            break;
                        } else {//inicia uma busca/comparacao de chassi desde a raiz
                            long retorno = buscaPosicao(0);
                            if (retorno == 0) {
                                System.out.println("Chassi já registrado em memoria");
                            } else {
                                grava(retorno);
                            }
                            break;
                        }
                    case 2:
                        System.out.println("Digite [1] para remover chassi ou [2] para remover por nome");
                        busca = imput.nextInt();
                        if (busca == 1) {
                            System.out.print("Digite o chassi a remover: ");
                            chassi = imput.nextLine();
                            chassi = imput.nextLine();
                        } else if (busca == 2) {
                            System.out.print("Digite o nome a remover: ");
                            name = imput.nextLine();
                            name = imput.nextLine();
                        } else {
                            System.out.println("Opção invalida");
                            break;
                        }
                        long L4 = buscador(0, busca);
                        if (L4 == -1) {
                            System.out.println("Arquivo não encontrado em memória");
                            break;
                        }
                        file.seek(L4);
                        file.writeChars(String.format("%1$30s", "***"));
                        file.seek(L4+60);
                        file.writeChars(String.format("%1$45s", "***"));
                        file.seek(L4 + 248);
                        file.writeBoolean(true);
                        System.out.println("Registro removido");
                        break;
                    case 3:
                        System.out.print("Digite o chassi a alterar: ");
                        chassi = imput.nextLine();
                        chassi = imput.nextLine();
                        long L6 = buscador(0, 1);
                        System.out.println("Chassi encontrado. Agora escolha a opção para alterar");
                        System.out.println("1. Para nome");
                        System.out.println("2. Para cor");
                        System.out.println("3. Para valor");
                        System.out.println("4. Para quantidade");
                        Byte opt = imput.nextByte();
                        switch (opt) {
                            case 1:
                                System.out.print("Digite novo nome: ");
                                String nome2 = imput.nextLine();
                                nome2 = imput.nextLine();
                                file.seek(L6 + 60);
                                file.writeChars(String.format("%1$45s", nome2));
                                System.out.println("Nome alterado");
                                break;
                            case 2:
                                System.out.print("Digite nova cor: ");
                                String cor2 = imput.nextLine();
                                cor2 = imput.nextLine();
                                file.seek(L6 + 150);
                                file.writeChars(String.format("%1$35s", cor2));
                                System.out.println("Cor alterada");
                                break;
                            case 3:
                                System.out.print("Digite novo valor: ");
                                Double valor2 = imput.nextDouble();
                                file.seek(L6 + 220);
                                file.writeDouble(valor2);
                                System.out.println("Valor alterado");
                                break;
                            case 4:
                                System.out.print("Digite nova quantidade: ");
                                int qtde2 = imput.nextInt();
                                file.seek(L6 + 228);
                                file.writeInt(qtde2);
                                System.out.println("Quantidade alterada");
                                break;
                        }
                        break;
                    case 4:
                        System.out.print("Digite [1] para consultar chassi ou [2] para consultar por nome: ");
                        busca = imput.nextInt();
                        if (busca == 1) {
                            System.out.print("Digite o chassi a consultar: ");
                            chassi = imput.nextLine();
                            chassi = imput.nextLine();
                        } else if (busca == 2) {
                            System.out.print("Digite o nome a consultar: ");
                            name = imput.nextLine();
                            name = imput.nextLine();
                        } else {
                            System.out.println("Opção invalida");
                            break;
                        }
                        long L7 = buscador(0, busca);
                        System.out.println(imprimir(L7));
                        break;
                    case 5:
                        printTree(0, 0);
                        break;
                    case 6:
                        System.out.println("Sistema encerrado com: " + arq.length() + " bytes.");
                        file.close();
                        imput.close();
                        break;
                }
            }
        } catch (IOException e) {
            System.exit(1);
        }
    }
}
