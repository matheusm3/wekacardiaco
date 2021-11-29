package weka;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;

import weka.classifiers.lazy.IBk;
import weka.core.DenseInstance;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;


public class Main {

	public JFrame frame;
	public String[] valoresValidos = {"Não","Sim"};
	
	public static void main(String[] args) throws Exception {
		
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Main window = new Main();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	/**
	 * Create the application.
	 */
	public Main() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 * @throws Exception 
	 */
	private void initialize(){
		

		frame = new JFrame();
		frame.setBounds(100, 100, 450, 320);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JLabel lblNewLabel = new JLabel("Fadiga:");
		lblNewLabel.setBounds(46, 28, 61, 16);
		frame.getContentPane().add(lblNewLabel);
		
		JComboBox combofadiga = new JComboBox(valoresValidos);
		combofadiga.setBounds(46, 47, 84, 27);
		frame.getContentPane().add(combofadiga);
		
		JLabel lblNewLabel_1 = new JLabel("Palpitação:");
		lblNewLabel_1.setBounds(180, 28, 84, 16);
		frame.getContentPane().add(lblNewLabel_1);
		
		JComboBox combofaltaar = new JComboBox(valoresValidos);
		combofaltaar.setBounds(300, 47, 84, 27);
		frame.getContentPane().add(combofaltaar);
		
		JLabel lblNewLabel_2 = new JLabel("Falta de ar:");
		lblNewLabel_2.setBounds(300, 28, 84, 16);
		frame.getContentPane().add(lblNewLabel_2);
		
		JComboBox combopalpitacao = new JComboBox(valoresValidos);
		combopalpitacao.setBounds(180, 47, 84, 27);
		frame.getContentPane().add(combopalpitacao);
		
		JLabel lblNewLabel_1_1 = new JLabel("Batimento lento:");
		lblNewLabel_1_1.setBounds(180, 96, 108, 16);
		frame.getContentPane().add(lblNewLabel_1_1);
		
		JComboBox combobatlento = new JComboBox(valoresValidos);
		combobatlento.setBounds(180, 113, 84, 27);
		frame.getContentPane().add(combobatlento);
		
		JLabel lblNewLabel_1_1_2 = new JLabel("Dor no braço:");
		lblNewLabel_1_1_2.setBounds(300, 96, 108, 16);
		frame.getContentPane().add(lblNewLabel_1_1_2);
		
		JComboBox combobraco = new JComboBox(valoresValidos);
		combobraco.setBounds(300, 113, 84, 27);
		frame.getContentPane().add(combobraco);
		
		JLabel lblNewLabel_1_2 = new JLabel("Tontura:");
		lblNewLabel_1_2.setBounds(46, 94, 84, 16);
		frame.getContentPane().add(lblNewLabel_1_2);
		
		JComboBox combotontura = new JComboBox(valoresValidos);
		combotontura.setBounds(46, 113, 84, 27);
		frame.getContentPane().add(combotontura);
		
		JTextArea textArea = new JTextArea("** DIAGNÓSTICO RÁPIDO **");
		textArea.setBounds(23, 202, 403, 73);
		frame.getContentPane().add(textArea);
		
		JButton btn_obter = new JButton("Obter");
		btn_obter.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//JOptionPane.showMessageDialog(null, "Favor informar o usuário");
				String resultado = null;
				try {
					 resultado = valoresParaWeka(dePara((String)combofadiga.getSelectedItem()),
												dePara((String)combotontura.getSelectedItem()),
												dePara((String)combopalpitacao.getSelectedItem()),
												dePara((String)combofaltaar.getSelectedItem()),
												dePara((String)combobatlento.getSelectedItem()),
												dePara((String)combobraco.getSelectedItem()));
				} catch (Exception e1) {
					e1.printStackTrace();
				}
				
				textArea.setText("** DIAGNÓSTICO RÁPIDO **");
				textArea.append("\n\nSeus sintomas se assemelham a:\n");
				textArea.append(resultado);
			}
		});
		
		btn_obter.setBounds(165, 161, 117, 29);
		frame.getContentPane().add(btn_obter);

	}
	
	public String valoresParaWeka(String fadiga, String tontura, String palpitacao, String faltar, String batimento, String dorbraco
) throws Exception {
		
		DataSource ds = new DataSource("src/weka/cardiaco.arff");
		
		Instances ins = ds.getDataSet();
		ins.setClassIndex(6);
		
		
		IBk ibk = new IBk(3);
        ibk.buildClassifier(ins);
        
        Instance n = new DenseInstance(6);
		
        n.setDataset(ins);
        n.setValue(0, fadiga);
        n.setValue(1, tontura);
        n.setValue(2, palpitacao);
        n.setValue(3, faltar);
        n.setValue(4, batimento);
        n.setValue(5, dorbraco);
		
        double probWeka[] = ibk.distributionForInstance(n);
       
        double maior = probWeka[0];
        
        if (probWeka[1]>maior)
        	maior=probWeka[1];
        if (probWeka[2]>maior)
        	maior=probWeka[2];
        
        if (maior == probWeka[0])
        	return "PRESSAO ALTA";
        else if (maior == probWeka[1])
        	return "ARRITMIA CARDIACA";
        else if (maior == probWeka[2])
        	return "ATAQUE CARDIACO";
		
        
        return "FAVOR TENTAR NOVAMENTE!";	
	}
	
	// TRANSFORMA VALOR DO COMBOBOX PARA COMPREENSAO DO WEKA
	private String dePara(String resposta) {
		return (resposta == "Sim") ? "S" :  "N";
	}
}
