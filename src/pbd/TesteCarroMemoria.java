/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pbd;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import javax.swing.JOptionPane;

public class TesteCarroMemoria {

	// ESTE BLOCO CONTÉM REGISTROS DE 249 BYTES
	static String chassi = "";// 60 bytes
	static String name = "";// 90 bytes
	static String color = "";// 70 bytes
	static double value = 0;// 8 bytes
	static int quantity = 0;// 4 bytes
	static int busca = 0;// registra opcao de busca do usuario, chassi ou nome;
	static int opcao = 0;// registra opcao do usuario no menu
	static ComparatorChassi chassiComparator = new ComparatorChassi();

	// MANIPULAÇÃO DE ARQUIVO
	static RandomAccessFile file;
	static long fileSize = 0;// codigo do Tiago
	static String nomeArquivo = "texto.txt";// codigo do Tiago

	public static int painelInicial() {
		String option = JOptionPane.showInputDialog(null,
				"ESCOLHA UMA OPCAO" + "\r\n" + "1. Adicionar chassi" + "\r\n" + "2. Remover chassi" + "\r\n"
						+ "3. Alterar chassi" + "\r\n" + "4. Consultar chassi" + "\r\n" + "5. Imprimir arvore" + "\r\n"
						+ "6. Fechar sistema");
		return Integer.parseInt(option);
	}

	public static String openFile(String word1) {
		return JOptionPane.showInputDialog(null, "Arquivo aberto com tamanho atual de " + fileSize + " bytes." + "\r\n"
				+ "Informe o " + word1 + " do veículo:");
	}

	public static String updateHelper(String word1, boolean cond) {
		if (cond) {
			return JOptionPane.showInputDialog(null, "Digite o " + word1 + " a remover:");
		}
		return JOptionPane.showInputDialog(null, "Digite o " + word1 + " a consultar:");
	}

	public static String changeHelper(String word1, boolean cond) {
		if (cond) {
			return JOptionPane.showInputDialog(null, "Digite o parâmetro para: " + word1);
		}
		JOptionPane.showMessageDialog(null, "Alteração do parâmetro " + word1 + " efetuada.");
		return "";
	}

	public static void main(String[] args) {	
		try {
			File arq = new File(nomeArquivo);// objeto para manipulacao fisica do arquivo
			file = new RandomAccessFile(arq, "rw");// Abre arquivo para leitura e gravacao
			fileSize = file.length();// recupera o tamanho do arquivo
			
			Recorder recorder = new Recorder(file);
			PositionSearcher positionSearcher = new PositionSearcher(file);
			ChassiSearcher chassiSearcher = new ChassiSearcher(file);
			Printer printer = new Printer(file);
			TreePrinter treePrinter = new TreePrinter(file);
			
			while (opcao != 6) {
				opcao = painelInicial();
				
				switch (opcao) {
				case 1:
					chassi = openFile("chassi");

					name = openFile("nome");

					color = openFile("cor");

					value = Double.parseDouble(openFile("value"));

					quantity = Integer.parseInt(openFile("quantidade"));

					if (fileSize == 0) {// tamanho zero, basta gravar, nao precisa checagem
						recorder.grava(0);
						break;
					} else {// inicia uma busca/comparacao de chassi desde a raiz
						long retorno = positionSearcher.positionSearcher(0);
						if (retorno == 0) {
							JOptionPane.showMessageDialog(null, "Chassi já registrado em memoria");
						} else {
							recorder.grava(retorno);
						}
						break;
					}
				case 2:
					String answer = JOptionPane.showInputDialog(null,
							"Digite [1] para remover chassi ou [2] " + "para remover por nome");
					busca = Integer.parseInt(answer);
					if (busca == 1) {
						chassi = updateHelper("chassi", true);
					} else if (busca == 2) {
						name = updateHelper("nome", true);
					} else {
						JOptionPane.showMessageDialog(null, "Opção invalida");
						break;
					}
					long L4 = chassiSearcher.searcher(0, busca);
					if (L4 == -1) {
						JOptionPane.showMessageDialog(null, "Arquivo não encontrado em memória");
						break;
					}
					file.seek(L4);
					file.writeChars(String.format("%1$30s", "***"));
					file.seek(L4 + 60);
					file.writeChars(String.format("%1$45s", "***"));
					file.seek(L4 + 248);
					file.writeBoolean(true);
					JOptionPane.showMessageDialog(null, "Registro removido");
					break;
				case 3:
					chassi = JOptionPane.showInputDialog(null, "Digite o chassi a alterar:");
					long L6 = chassiSearcher.searcher(0, 1);
					answer = JOptionPane.showInputDialog(null,
							"Chassi encontrado. Agora escolha a opção para alterar" + "\r\n" + "1. Para nome" + "\r\n"
									+ "2. Para cor" + "\r\n" + "3. Para valor" + "\r\n" + "4. Para quantidade");
					int opt = Integer.parseInt(answer);
					switch (opt) {
					case 1:
						String nome2 = changeHelper("nome", true);
						file.seek(L6 + 60);
						file.writeChars(String.format("%1$45s", nome2));
						changeHelper("nome", false);
						break;
					case 2:
						String cor2 = changeHelper("cor", true);
						file.seek(L6 + 150);
						file.writeChars(String.format("%1$35s", cor2));
						changeHelper("cor", false);
						break;
					case 3:
						Double valor2 = Double.parseDouble(changeHelper("valor", true));
						file.seek(L6 + 220);
						file.writeDouble(valor2);
						changeHelper("valor", false);
						break;
					case 4:
						int qtde2 = Integer.parseInt(changeHelper("quantidade", true));
						file.seek(L6 + 228);
						file.writeInt(qtde2);
						changeHelper("quantidade", false);
						break;
					}
					break;
				case 4:
					answer = JOptionPane.showInputDialog(null,
							"Digite [1] para consultar chassi ou [2] para consultar por nome: ");
					busca = Integer.parseInt(answer);
					if (busca == 1) {
						chassi = updateHelper("chassi", false);
					} else if (busca == 2) {
						name = updateHelper("nome", false);
					} else {
						JOptionPane.showMessageDialog(null, "Opção invalida");
						break;
					}
					long L7 = chassiSearcher.searcher(0, busca);
					System.out.println(printer.printer(L7));
					break;
				case 5:
					treePrinter.printTree(0, 0);
					break;
				case 6:
					JOptionPane.showMessageDialog(null, "Sistema encerrado com: " + arq.length() + " bytes.");
					file.close();
					arq.delete();
					break;
				}
			}
		} catch (IOException e) {
			System.err.println("Erro no Main: " + e);
		}
	}
}
