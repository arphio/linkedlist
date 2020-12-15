package poo.util;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.StringTokenizer;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.event.ChangeListener;
import javax.swing.text.Caret;
import javax.swing.text.DefaultCaret;
import javax.swing.text.JTextComponent;

public class GUI extends JFrame{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2915328158113754338L;
	
	private File saveFile;
	
	private List<Integer> list=new LinkedList<>();
	
	private JPanel panel1,panel2,panel3;
	private Font f= new Font(Font.MONOSPACED,Font.PLAIN, 12);
	private JTextArea text;
	private JTextField element;
	private JLabel etiq;
	private JButton agCoda, agTesta, rimCoda, rimTesta;
	
	private JMenuBar menu;
	private JMenu op,file,agg,rim;
	private JMenuItem salva,salvaNom,carica, exit, aggPos, rimPos, rimEl, help, ripr, iter;
	private Ascoltatore listener=new Ascoltatore();
	private frameAggiungiPos fAP=null;
	private FrameRimPos fRP=null;
	private FrameRimEl fRE=null;
	private FrameIteratore fI=null;
	private FrameHelp fH=null;
	private enum mov{FORWARD, BACKWARD, UNKNOWN};
	
	//boolean saved=false;
	
	
	
	public GUI() {
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		
		addWindowListener( new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				if(consensoUscita()) System.exit(0);
			}
		});
		
		setTitle("LinkedList");
		setSize(1000,1000); setLocation(200,300);
		
		add(panel1=new JPanel(), BorderLayout.NORTH);
		panel1.add(text=new JTextArea(2,30)); text.setText("[]");
		
		add(panel2=new JPanel(), BorderLayout.CENTER);
		add(panel3=new JPanel(), BorderLayout.SOUTH);
		panel2.add(etiq=new JLabel("Elemento: "));
		panel2.add(element=new JTextField("",5));
		panel2.add(agCoda=new JButton("Aggiunta in coda ")); panel2.add(agTesta=new JButton("Aggiunta in testa "));
		panel3.add(rimCoda=new JButton("Rimozione in coda ")); panel3.add(rimTesta=new JButton("Rimozione in testa "));
		text.setEditable(false);
		text.setFont(f);
		
		setJMenuBar(menu=new JMenuBar());
		menu.add(file=new JMenu("File"));
		menu.add(op=new JMenu("Operations"));
		menu.add(help=new JMenuItem("Help"));
		file.add(salva=new JMenuItem("Salva"));
		file.add(salvaNom=new JMenuItem("Salva con nome..."));
		file.add(carica=new JMenuItem("Carica"));
		file.addSeparator();
		file.add(exit=new JMenuItem("Exit"));
		op.add(agg=new JMenu("Aggiungi"));
		op.add(rim=new JMenu("Rimuovi"));
		op.add(iter=new JMenuItem("Iteratore"));
		op.add(ripr=new JMenuItem("Ripristina tutto"));
		agg.add(aggPos=new JMenuItem("Aggiungi in posizione"));
		rim.add(rimPos=new JMenuItem("Rimuovi in posizione"));
		rim.add(rimEl=new JMenuItem("Rimuovi elemento"));
		//aggiunta listener
		aggPos.addActionListener(listener);
		rimPos.addActionListener(listener);
		rimEl.addActionListener(listener);
		exit.addActionListener(listener);
		help.addActionListener(listener);
		salva.addActionListener(listener);
		salvaNom.addActionListener(listener);
		carica.addActionListener(listener);
		agCoda.addActionListener(listener);
		agTesta.addActionListener(listener);
		rimCoda.addActionListener(listener);
		rimTesta.addActionListener(listener);
		ripr.addActionListener(listener);
		iter.addActionListener(listener);
		setResizable(false);
		pack();
		System.out.println(list);
	}
	
	/*private String setIteratore(int pos) {
		StringBuilder sb=new StringBuilder();
		Iterator<Integer> it=list.iterator();
		int c=-1; int len=0;
		while(it.hasNext() && c<pos) {
			int e=it.next(); c++;
			String s=Integer.toString(e);
			len+=s.length();
		}
		len=len+(2*pos);
		sb.append("\n");
		for(int i=0; i<len-1; i++) sb.append(" "); //tolgo un 1 perchè voglio che rientri sulla parentesi
		sb.append("^");
		System.out.println(len);
		return sb.toString();
	}*/
	
	private int avanti(Iterator<Integer> it) {
		int x=it.next(); String s=Integer.toString(x);
		String t=text.getText();
		StringBuilder sb=new StringBuilder();
		sb.append(t.substring(0, t.length()-1)); //tolgo il precedente '^'
		for(int i=0; i<s.length()+2; i++) sb.append(" ");
		sb.append("^");
		text.setText(sb.toString());
		return x;
	}
	
	private int indietro(ListIterator<Integer> lit) {
		int x=lit.previous(); String s=Integer.toString(x);
		String t=text.getText();
		StringBuilder sb=new StringBuilder();
		sb.append(t.substring(0, t.length()-3-s.length()));
		sb.append("^");
		text.setText(sb.toString());
		return x;
	}
	
	private void rimuoviF(Iterator<Integer> it) {
		//remove utilizzata dopo uno spostamento in avanti
		int lenIn=list.toString().length();
		it.remove();
		/*if(list.size()==0) {
			text.setText(list.toString());
			text.append("\n"); text.append("^");
			return;
		}*/
		int lenFi=list.toString().length();
		int len=lenIn-lenFi;
		String t=text.getText();
		String sub=t.substring(lenIn+1+len, t.length());
		text.setText(list.toString()+"\n"+sub);
	}
	
	private void rimuoviB(Iterator<Integer>it) {
		//remove utilizzata dopo una previus
		int lenIn=list.toString().length();
		it.remove();
		String t=text.getText();
		String sub=t.substring(lenIn+1, t.length());
		text.setText(list.toString()+"\n"+sub);
	}
	
	private void aggiungi(ListIterator<Integer>lit, int e) {
		int lenIn=list.toString().length();
		lit.add(e);
		int lenFi=list.toString().length();
		String sub=text.getText().substring(lenIn+1,text.getText().length());
		int marg=lenFi-lenIn;
		text.setText(list.toString());
		text.append("\n"); for(int i=0; i<marg; i++) text.append(" ");
		text.append(sub);
		/*
		String x=Integer.toString(e);
		String t=text.getText();
		int marg=t.length()-list.toString().length();//margine dove era presente il '^'
		lit.add(e);
		StringBuilder sb=new StringBuilder();
		sb.append(list.toString()); sb.append("\n");
		for(int i=0; i<marg+x.length(); i++) sb.append(" ");
		sb.append("^");
		text.setText(sb.toString());*/
	}
	
	private ListIterator<Integer> nuovoIteratore(){
		/*ListIterator<Integer> lit=list.listIterator();
		text.setText(list.toString());
		text.append("\n");
		text.append("^");
		return lit;*/
		return nuovoIteratore(0);
	}
	
	private ListIterator<Integer> nuovoIteratore(int pos){
		ListIterator<Integer>lit=list.listIterator(pos);
		text.setText(list.toString());
		text.append("\n");
		int len=0;
		int c=0;
		Iterator<Integer> it=list.iterator();
		while(it.hasNext() && c<pos) {
			len+=Integer.toString(it.next()).length();
			c++;
		}
		for(int i=0; i<len+2*pos; i++) text.append(" ");
		text.append("^");
		return  lit;
	}
	
	
	private boolean consensoUscita() {
		if(!salva.isEnabled()) return true;
		int option=JOptionPane.showConfirmDialog(null, "Continuare? Uscendo tutti" + 
				" i dati non salvati andranno persi!", null, JOptionPane.YES_NO_OPTION);
		return option==JOptionPane.YES_OPTION;
	}
	
	private void ripristina() {
		try {
			list.ripristina(saveFile.getAbsolutePath());
			salva.setEnabled(false);
		}catch(IOException ex) {
			JOptionPane.showMessageDialog(null, "Ripristino fallito!", "ATTENZIONE", JOptionPane.ERROR_MESSAGE);
		}
		System.out.println(list);
		text.setText(list.toString());
	}
	
	private void salva() {
		try {
			list.salva(saveFile.getAbsolutePath());
			salva.setEnabled(false);
		}catch(IOException ex){
			salva.setEnabled(true);
			JOptionPane.showMessageDialog(null, "Salvataggio non riuscito!", "ATTENZIONE", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	
	private class FrameIteratore extends JFrame implements ActionListener{

		/**
		 * 
		 */
		private static final long serialVersionUID = 6896226841856535405L;
		JButton next,prev,add,rem;
		JTextField valAdd, nextInd, prevInd;
		JPanel pan1,pan2;
		ListIterator<Integer>lit;
		mov m;
		JLabel lNeIn=new JLabel("Next index: ");
		JLabel lPrIn=new JLabel("Previous index: ");
		
		public FrameIteratore(int pos) {
			setLocation(150,150);
			setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
			
			addWindowListener( new WindowAdapter() {
				public void windowClosing(WindowEvent e) {
					if(consensoUscitaIt()) {
						dispose();
						agCoda.setEnabled(true); agTesta.setEnabled(true);
						rimCoda.setEnabled(true); rimTesta.setEnabled(true);
						text.setText(list.toString());
					}
				}
			});
			
			add(pan1=new JPanel(), BorderLayout.NORTH);
			add(pan2=new JPanel(), BorderLayout.SOUTH);
			pan1.add(valAdd=new JTextField("",5)); pan1.add(add=new JButton("Add"));
			pan2.add(next=new JButton("Next")); pan2.add(prev=new JButton("Previous"));
			pan2.add(rem=new JButton("Remove"));
			add.addActionListener(this);
			rem.addActionListener(this);
			next.addActionListener(this);
			prev.addActionListener(this);
			lit=nuovoIteratore(pos);
			pan1.add(lNeIn);
			pan1.add(nextInd=new JTextField(Integer.toString(lit.nextIndex()), 2), JLabel.RIGHT_ALIGNMENT);
			pan1.add(lPrIn);
			pan1.add(prevInd=new JTextField(Integer.toString(lit.previousIndex()),2), JLabel.RIGHT_ALIGNMENT);
			nextInd.setEditable(false); prevInd.setEditable(false);
			checkButtons(); rem.setEnabled(false);
			m=mov.UNKNOWN; 
			pack();
		}
		
		public FrameIteratore() { this(0);}

		@Override
		public void actionPerformed(ActionEvent e) {
			if(e.getSource()==next) {
				avanti(lit); prev.setEnabled(true);
				if(!lit.hasNext())next.setEnabled(false);
				rem.setEnabled(true);
				m=mov.FORWARD;
				updateIndexes();
			}
			else if(e.getSource()==prev) {
				indietro(lit); next.setEnabled(true);
				if(!lit.hasPrevious())prev.setEnabled(false);
				rem.setEnabled(true);
				m=mov.BACKWARD;
				updateIndexes();
			}
			else if(e.getSource()==rem) {
				if(m==mov.FORWARD) rimuoviF(lit);
				else if(m==mov.BACKWARD) rimuoviB(lit);
				if(!lit.hasNext())next.setEnabled(false);
				if(!lit.hasPrevious()) prev.setEnabled(false);
				rem.setEnabled(false);
				updateIndexes(); checkButtons();
			}
			else if(e.getSource()==add) {
				try {
					aggiungi(lit,Integer.parseInt(valAdd.getText()));
					rem.setEnabled(false);
					updateIndexes(); checkButtons();
				}catch(Exception ex){
					JOptionPane.showMessageDialog(null, "Inserire un numero!", "ATTENZIONE", JOptionPane.ERROR_MESSAGE);
				}
			}
		}
		
		private void checkButtons() {
			if(!lit.hasNext()) next.setEnabled(false);
			else next.setEnabled(true);
			if(!lit.hasPrevious())prev.setEnabled(false);
			else prev.setEnabled(true);
		}
		private void updateIndexes() {
			prevInd.setText(Integer.toString(lit.previousIndex()));
			nextInd.setText(Integer.toString(lit.nextIndex()));
		}
		
		private boolean consensoUscitaIt() {
			int op=JOptionPane.showConfirmDialog(null, "Chiudendo la finestra perderai l'iteratore. Uscire?", "ATTENZIONE", JOptionPane.OK_CANCEL_OPTION);
			return op==JOptionPane.OK_OPTION;
		}
		
	}
	
	private class FrameHelp extends JFrame{
		
	}
	
	
	private class FrameRimEl extends JFrame implements ActionListener{
		/**
		 * 
		 */
		private static final long serialVersionUID = -6282729502515061843L;
		JButton ok;
		JLabel elemento;
		JPanel pan;
		JTextField valEl;
		
		public FrameRimEl() {
			ok=new JButton("OK");
			elemento=new JLabel("Elemento :");
			pan=new JPanel();
			valEl=new JTextField("",5);
			pan.add(elemento);
			pan.add(valEl);
			pan.add(ok);
			add(pan);
			pack();
			setLocation(150,150);
			
			ok.addActionListener(this);
			valEl.addActionListener(this);
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			if(e.getSource()==ok) {
				String x=valEl.getText();
					try {
						Integer i=Integer.parseInt(x);
						Iterator<Integer>it=nuovoIteratore();
						while(it.hasNext()) {
							int n=avanti(it);
							if(n==i) {
								rimuoviF(it); break;
							}
						}
						System.out.println(list);
						salva.setEnabled(true);
					}catch(Exception ex) {
						System.out.println("Non è un intero!");
						JOptionPane.showMessageDialog(null, "Inserire un numero!", "ATTENZIONE", JOptionPane.ERROR_MESSAGE);
					}
			}
		}
		
	}
	
	private class FrameRimPos extends JFrame implements ActionListener{
		/**
		 * 
		 */
		private static final long serialVersionUID = -6126521130626521640L;
		JButton ok;
		JPanel pan;
		JLabel pos;
		JTextField valPos;
		
		public FrameRimPos() {
			ok=new JButton("OK");	
			pan=new JPanel();
			pos=new JLabel("Posizione ");
			valPos= new JTextField("",5);
			
			pan.add(pos); pan.add(valPos);
			pan.add(ok);
			add(pan);
			ok.addActionListener(this);
			setLocation(150,150);
			pack();
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			if(e.getSource()==ok) {
				String x=valPos.getText();
					try {
						int c=-1;
						Integer i=Integer.parseInt(x);
						if(i<0 || i>=list.size()) throw new IndexOutOfBoundsException();
						Iterator<Integer> it=nuovoIteratore();
						while(it.hasNext()&&c<i) {
							avanti(it); c++;
						}
						rimuoviF(it);
						salva.setEnabled(true);
					}catch(IndexOutOfBoundsException ex) {
						System.out.println("Non è un numero!");
						JOptionPane.showMessageDialog(null, "Inserire posizione valida!", "ATTENZIONE", JOptionPane.ERROR_MESSAGE);
					}
			}
		}
		
	}
	
	private class frameAggiungiPos extends JFrame implements ActionListener{
		/**
		 * 
		 */
		private static final long serialVersionUID = 5087571918675029924L;
		JButton ok, close;
		JLabel elemento, pos;
		JPanel pan;
		JTextField valEl, valPos;
		public frameAggiungiPos(){
			pan=new JPanel();
			ok=new JButton("OK"); close=new JButton("Chiudi");
			elemento=new JLabel("Elemento: ");
			pos=new JLabel("Posizione :");
			valEl=new JTextField("",5);
			valPos=new JTextField("",5);
			pan.add(elemento); pan.add(valEl);
			pan.add(pos); pan.add(valPos);
			pan.add(ok); pan.add(close);
			add(pan);
			setLocation(150,150);
			pack();
			
			ok.addActionListener(this);
			close.addActionListener(this);
		}
		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			if(e.getSource()==ok) {
				//try {
					int c=0;
					Integer posto=Integer.parseInt(valPos.getText());
					if(posto>list.size() || posto<0) {
						JOptionPane.showMessageDialog(null, "Posizione non valida!", "ATTENZIONE", JOptionPane.ERROR_MESSAGE);
						return;
					}
					Integer numero=Integer.parseInt(valEl.getText());
					ListIterator<Integer> lit=nuovoIteratore();
					while(lit.hasNext() && c<posto) {
						avanti(lit); c++;
					}
					if(c==posto) { aggiungi(lit,numero);  salva.setEnabled(true);}
				//}catch(Exception ex) {salva.setEnabled(false);
				//   JOptionPane.showMessageDialog(null, "Inserire un numero!", "ATTENZIONE!", JOptionPane.ERROR_MESSAGE);
				//}
			System.out.println(list);
			//text.setText(list.toString());
			}
			else if(e.getSource()==close) dispose();
		}
	}
	
	private class Aiuto extends JFrame{
		
	}
	
	private class Ascoltatore implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			if(e.getSource()==aggPos) {
				if(fAP==null)fAP=new frameAggiungiPos();
				fAP.setVisible(true);
			}
			else if(e.getSource()==exit) {
				if(consensoUscita()) System.exit(0);
			}
			else if(e.getSource()==help) {
			}
			else if(e.getSource()==salva) {
				if(saveFile==null) {
					JFileChooser chooser=new JFileChooser();
					if(chooser.showOpenDialog(null)==JFileChooser.APPROVE_OPTION) {
						saveFile=chooser.getSelectedFile();
					}
				}
				if(saveFile!=null) {
					salva();
				}
					
			}
			else if(e.getSource()==salvaNom) {
				JFileChooser chooser=new JFileChooser();
				if(chooser.showOpenDialog(null)==JFileChooser.APPROVE_OPTION) {
					saveFile=chooser.getSelectedFile();
				}
				if(saveFile!=null)
					salva();
			}
			else if(e.getSource()==carica) {
				JFileChooser chooser=new JFileChooser();
				if(chooser.showOpenDialog(null)==JFileChooser.APPROVE_OPTION) {
					saveFile=chooser.getSelectedFile();
					ripristina();
				}
			}
			
			else if(e.getSource()==agCoda) {
				String x=element.getText();
				try {
					Integer i=Integer.parseInt(x);
					list.addLast(i);
					text.setText(list.toString());
					salva.setEnabled(true);
				}catch(Exception ex) {
					JOptionPane.showMessageDialog(null, "Inserire un numero!", "ATTENZIONE", JOptionPane.ERROR_MESSAGE);
				}
			}
			else if(e.getSource()==agTesta) {
				String x=element.getText();
				try {
					Integer i=Integer.parseInt(x);
					list.addFirst(i);
					text.setText(list.toString());
					salva.setEnabled(true);
				}catch(NumberFormatException ex) {
					JOptionPane.showMessageDialog(null, "Inserire un numero!", "ATTENZIONE", JOptionPane.ERROR_MESSAGE);
				}
			}
			else if(e.getSource()==rimCoda) {
				if(list.size()==0)return;
				list.removeLast();
				text.setText(list.toString());
				salva.setEnabled(true);
			}
			else if(e.getSource()==rimTesta) {
				if(list.size()==0)return;
				list.removeFirst();
				text.setText(list.toString());
				salva.setEnabled(true);
			}
			
			else if(e.getSource()==rimPos) {
				fRP=new FrameRimPos();
				fRP.setVisible(true);
			}
			else if(e.getSource()==rimEl) {
				fRE=new FrameRimEl();
				fRE.setVisible(true);
			}
			else if(e.getSource()==ripr) {
				if(saveFile==null) {
					list.clear();
					text.setText(list.toString());
				}
				else ripristina();
			}
			else if(e.getSource()==iter) {
				fI=new FrameIteratore();
				fI.setVisible(true);
				agCoda.setEnabled(false);
				agTesta.setEnabled(false);
				rimCoda.setEnabled(false);
				rimTesta.setEnabled(false);
			}
			
			else if(e.getSource()==help) {
				fH=new FrameHelp();
				fH.setVisible(true);
			}
		}
		
	}
	

	public static void main(String[] args) {
		EventQueue.invokeLater( new Runnable(){
			  public void run(){
			      JFrame f=new GUI();
			      f.setVisible(true);			  
			  }
		  });	   

	}

}
