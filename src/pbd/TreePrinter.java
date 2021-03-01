package pbd;

import java.io.RandomAccessFile;

public class TreePrinter {
	private RandomAccessFile file;
	private Printer printer;
	
	public TreePrinter(RandomAccessFile file2) {
		this.file = file2;
		this.printer = new Printer(file2);
	}

	public void printTree(long mem, int nivel){
        long right = 0;
        long left = 0;
        String level = "";
        
        try {
            file.seek(mem+232);
            left = file.readLong();
            file.seek(mem+240);
            right = file.readLong();
            
            if(mem == 0)
                System.out.println("RAIZ: " + printer.printer(mem));
            
            for (int i = 0; i <= nivel; i++) 
                level +="-----|";
            
            if(right != -1){
                System.out.println(level + "Filho direito: " + printer.printer(right));
                printTree(right, nivel+1);
            }
            if(left != -1){
                System.out.println(level + "Filho esquerdo: " +printer.printer(left));
                printTree(left, nivel+1);
            }
        } catch (Exception e) {
            System.err.println("Erro no Tree Printer: " + e);
        }
	}

}
