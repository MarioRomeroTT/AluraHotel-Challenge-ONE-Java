package views;

import java.awt.Color;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.sql.Date;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import com.mysql.cj.protocol.a.LocalDateTimeValueEncoder;

import jdbc.controller.HuespedesController;
import jdbc.controller.ReservasController;
import jdbc.modelo.Huespedes;
import jdbc.modelo.Reservas;

@SuppressWarnings("serial")
public class Busqueda extends JFrame {

	private JPanel contentPane;
	private JTextField txtBuscar;
	private JTable tbHuespedes;
	private JTable tbReservas;
	private DefaultTableModel modelo;
	private DefaultTableModel modeloHuesped;
	private JLabel labelAtras;
	private JLabel labelExit;
	int xMouse, yMouse;
	private ReservasController reservasController;
	private HuespedesController huespedesController;
	

	/**
	 * Create the frame.
	 */
	public Busqueda() {
		
		this.reservasController = new ReservasController();
		this.huespedesController = new HuespedesController();
		
		setIconImage(Toolkit.getDefaultToolkit().getImage(Busqueda.class.getResource("/imagenes/lupa2.png")));
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 910, 571);
		contentPane = new JPanel();
		contentPane.setBackground(Color.WHITE);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		setLocationRelativeTo(null);
		setUndecorated(true);
		
		txtBuscar = new JTextField();
		txtBuscar.setBounds(536, 127, 193, 31);
		txtBuscar.setBorder(javax.swing.BorderFactory.createEmptyBorder());
		contentPane.add(txtBuscar);
		txtBuscar.setColumns(10);
		
		
		JLabel lblNewLabel_4 = new JLabel("SISTEMA DE BÚSQUEDA");
		lblNewLabel_4.setForeground(new Color(12, 138, 199));
		lblNewLabel_4.setFont(new Font("Roboto Black", Font.BOLD, 24));
		lblNewLabel_4.setBounds(331, 62, 280, 42);
		contentPane.add(lblNewLabel_4);
		
		JTabbedPane panel = new JTabbedPane(JTabbedPane.TOP);
		panel.setBackground(new Color(12, 138, 199));
		panel.setFont(new Font("Roboto", Font.PLAIN, 16));
		panel.setBounds(20, 169, 865, 328);
		contentPane.add(panel);

		
		tbReservas = new JTable();
		tbReservas.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		tbReservas.setFont(new Font("Roboto", Font.PLAIN, 16));
		modelo = (DefaultTableModel) tbReservas.getModel();
		modelo.addColumn("Numero de Reserva");
		modelo.addColumn("Fecha Check In");
		modelo.addColumn("Fecha Check Out");
		modelo.addColumn("Valor");
		modelo.addColumn("Forma de Pago");
		JScrollPane scroll_table = new JScrollPane(tbReservas);
		panel.addTab("Reservas", new ImageIcon(Busqueda.class.getResource("/imagenes/reservado.png")), scroll_table, null);
		scroll_table.setVisible(true);
		tbReservas.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
				
		cargaTblReservas();
		
		tbHuespedes = new JTable();
		tbHuespedes.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		tbHuespedes.setFont(new Font("Roboto", Font.PLAIN, 16));
		modeloHuesped = (DefaultTableModel) tbHuespedes.getModel();
		modeloHuesped.addColumn("Número de Huesped");
		modeloHuesped.addColumn("Nombre");
		modeloHuesped.addColumn("Apellido");
		modeloHuesped.addColumn("Fecha de Nacimiento");
		modeloHuesped.addColumn("Nacionalidad");
		modeloHuesped.addColumn("Telefono");
		modeloHuesped.addColumn("Número de Reserva");
		JScrollPane scroll_tableHuespedes = new JScrollPane(tbHuespedes);
		panel.addTab("Huéspedes", new ImageIcon(Busqueda.class.getResource("/imagenes/pessoas.png")), scroll_tableHuespedes, null);
		scroll_tableHuespedes.setVisible(true);
	
		cargaTblHuespedes();
		 panel.addMouseListener(new MouseAdapter() {
	            @Override
	            public void mouseClicked(MouseEvent e) {
	                if (panel.getSelectedIndex() == 0) {
	                    limpiarTblReserva();
	                    cargaTblReservas();
	                } else if (panel.getSelectedIndex() == 1) {
	                	 limpiarTblHuesped();
	                	 cargaTblHuespedes();
	                }
	            }
	        });
		
		JLabel lblNewLabel_2 = new JLabel("");
		lblNewLabel_2.setIcon(new ImageIcon(Busqueda.class.getResource("/imagenes/Ha-100px.png")));
		lblNewLabel_2.setBounds(56, 51, 104, 107);
		contentPane.add(lblNewLabel_2);
		
		JPanel header = new JPanel();
		header.addMouseMotionListener(new MouseMotionAdapter() {
			@Override
			public void mouseDragged(MouseEvent e) {
				headerMouseDragged(e);
			     
			}
		});
		header.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				headerMousePressed(e);
			}
		});
		header.setLayout(null);
		header.setBackground(Color.WHITE);
		header.setBounds(0, 0, 910, 36);
		contentPane.add(header);
		
		JPanel btnAtras = new JPanel();
		btnAtras.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				MenuUsuario usuario = new MenuUsuario();
				usuario.setVisible(true);
				dispose();				
			}
			@Override
			public void mouseEntered(MouseEvent e) {
				btnAtras.setBackground(new Color(12, 138, 199));
				labelAtras.setForeground(Color.white);
			}			
			@Override
			public void mouseExited(MouseEvent e) {
				 btnAtras.setBackground(Color.white);
			     labelAtras.setForeground(Color.black);
			}
		});
		btnAtras.setLayout(null);
		btnAtras.setBackground(Color.WHITE);
		btnAtras.setBounds(0, 0, 53, 36);
		header.add(btnAtras);
		
		labelAtras = new JLabel("<");
		labelAtras.setHorizontalAlignment(SwingConstants.CENTER);
		labelAtras.setFont(new Font("Roboto", Font.PLAIN, 23));
		labelAtras.setBounds(0, 0, 53, 36);
		btnAtras.add(labelAtras);
		
		JPanel btnexit = new JPanel();
		btnexit.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				Login login = new Login();
				login.setVisible(true);
				dispose();
			}
			@Override
			public void mouseEntered(MouseEvent e) { //Al usuario pasar el mouse por el botón este cambiará de color
				btnexit.setBackground(Color.red);
				labelExit.setForeground(Color.white);
			}			
			@Override
			public void mouseExited(MouseEvent e) { //Al usuario quitar el mouse por el botón este volverá al estado original
				 btnexit.setBackground(Color.white);
			     labelExit.setForeground(Color.black);
			}
		});
		btnexit.setLayout(null);
		btnexit.setBackground(Color.WHITE);
		btnexit.setBounds(857, 0, 53, 36);
		header.add(btnexit);
		
		labelExit = new JLabel("X");
		labelExit.setHorizontalAlignment(SwingConstants.CENTER);
		labelExit.setForeground(Color.BLACK);
		labelExit.setFont(new Font("Roboto", Font.PLAIN, 18));
		labelExit.setBounds(0, 0, 53, 36);
		btnexit.add(labelExit);
		
		JSeparator separator_1_2 = new JSeparator();
		separator_1_2.setForeground(new Color(12, 138, 199));
		separator_1_2.setBackground(new Color(12, 138, 199));
		separator_1_2.setBounds(539, 159, 193, 2);
		contentPane.add(separator_1_2);
		
		JPanel btnbuscar = new JPanel();
		btnbuscar.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				
				 if (panel.getSelectedIndex() == 0) {
					 limpiarTblReserva();
					 buscarReservId();
					 limpaBuscador();
				 } else if (panel.getSelectedIndex() == 1){
					  limpiarTblHuesped();
				      buscarHuespedesApellido();
				      limpaBuscador();
				   }
				} 
		});
		btnbuscar.setLayout(null);
		btnbuscar.setBackground(new Color(12, 138, 199));
		btnbuscar.setBounds(748, 125, 122, 35);
		btnbuscar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
		contentPane.add(btnbuscar);
		
		JLabel lblBuscar = new JLabel("BUSCAR");
		lblBuscar.setBounds(0, 0, 122, 35);
		btnbuscar.add(lblBuscar);
		lblBuscar.setHorizontalAlignment(SwingConstants.CENTER);
		lblBuscar.setForeground(Color.WHITE);
		lblBuscar.setFont(new Font("Roboto", Font.PLAIN, 18));
		
		JPanel btnEditar = new JPanel();
		btnEditar.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				
				if(panel.getSelectedIndex() == 0) {
					modificarReserva();
					limpiarTblReserva();
					cargaTblReservas();
				}
				if(panel.getSelectedIndex() == 1) {
					modificarHuesped();
					limpiarTblHuesped();
					cargaTblHuespedes();
				}
			}
		});
		btnEditar.setLayout(null);
		btnEditar.setBackground(new Color(12, 138, 199));
		btnEditar.setBounds(635, 508, 122, 35);
		btnEditar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
		contentPane.add(btnEditar);
		
		JLabel lblEditar = new JLabel("EDITAR");
		lblEditar.setHorizontalAlignment(SwingConstants.CENTER);
		lblEditar.setForeground(Color.WHITE);
		lblEditar.setFont(new Font("Roboto", Font.PLAIN, 18));
		lblEditar.setBounds(0, 0, 122, 35);
		btnEditar.add(lblEditar);
		
		JPanel btnEliminar = new JPanel();
		btnEliminar.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				
				
				if(panel.getSelectedIndex() == 0) {
					int filaReserva = tbReservas.getSelectedRow();
					int idReserva = Integer.valueOf(tbReservas.getValueAt(filaReserva, 0).toString());
					int confirmar = JOptionPane.showConfirmDialog(null, "Desea borrar la Reserva N°: " + idReserva);
					if(confirmar == JOptionPane.YES_OPTION) {
						reservasController.eliminarReserva(idReserva);
						JOptionPane.showMessageDialog(contentPane, "Reserva eliminada con exito!");
						limpiarTblReserva();
						cargaTblReservas();
					}
				} else if(panel.getSelectedIndex() == 1) {
					int filaHuesped = tbHuespedes.getSelectedRow();
					int idHuesped = Integer.valueOf(tbHuespedes.getValueAt(filaHuesped, 0).toString());
					int confirmar = JOptionPane.showConfirmDialog(null, "Desea borrar el huesped N°: " + idHuesped);
					if(confirmar == JOptionPane.YES_OPTION) {
						huespedesController.eliminarHuesped(idHuesped);
						JOptionPane.showMessageDialog(contentPane, "Huesped eliminado con exito!");
						limpiarTblHuesped();
						cargaTblHuespedes();
					}
				}
			}
		});
		btnEliminar.setLayout(null);
		btnEliminar.setBackground(new Color(12, 138, 199));
		btnEliminar.setBounds(767, 508, 122, 35);
		btnEliminar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
		contentPane.add(btnEliminar);
		
		JLabel lblEliminar = new JLabel("ELIMINAR");
		lblEliminar.setHorizontalAlignment(SwingConstants.CENTER);
		lblEliminar.setForeground(Color.WHITE);
		lblEliminar.setFont(new Font("Roboto", Font.PLAIN, 18));
		lblEliminar.setBounds(0, 0, 122, 35);
		btnEliminar.add(lblEliminar);
		setResizable(false);
	}
	

private void cargaTblReservas() {
		List<Reservas> reservas = this.reservasController.listarReservas();
		
		reservas.forEach(reserva -> modelo.addRow(
				new Object[] {reserva.getId(),
								reserva.getFechaEntrada(),
								reserva.getFechaSalida(),
								reserva.getValor(),
								reserva.getFormaPago()}));
	}

	private void buscarReservId() {
		if (isNumeric(txtBuscar.getText())) {
			List<Reservas> reservas = this.reservasController.listarReservasId(Integer.valueOf(txtBuscar.getText()));
			if (reservas.isEmpty()) {
				JOptionPane.showMessageDialog(this, "El número de reserva no existe!", "WARNING",
						JOptionPane.WARNING_MESSAGE);
				cargaTblReservas();
				limpaBuscador();
			} else {
				reservas.forEach(reserva -> modelo.addRow(new Object[] { reserva.getId(), reserva.getFechaEntrada(),
						reserva.getFechaSalida(), reserva.getValor(), reserva.getFormaPago() }));
			}
		} else {
			JOptionPane.showMessageDialog(this, "Ingrese un criterio de busqueda valido!", "WARNING",
					JOptionPane.WARNING_MESSAGE);

			cargaTblReservas();
			limpaBuscador();
		}
	}

private void modificarReserva() {
	Optional.ofNullable(modelo.getValueAt(tbReservas.getSelectedRow(), tbReservas.getSelectedColumn()))
	.ifPresent(fila ->{
		
		Integer id = Integer.valueOf(modelo.getValueAt(tbReservas.getSelectedRow(), 0).toString());
		LocalDate fE = LocalDate.parse(modelo.getValueAt(tbReservas.getSelectedRow(), 1).toString());
		LocalDate fS = LocalDate.parse(modelo.getValueAt(tbReservas.getSelectedRow(), 2).toString());
				
		String valorRecalulado = recalulaValor(fE,fS);
		String valor = "$" + valorRecalulado ;
		String formaPago = (String) modelo.getValueAt(tbReservas.getSelectedRow(), 4);
		
		if (tbReservas.getSelectedColumn() == 0 || tbReservas.getSelectedColumn() == 5) {
			JOptionPane.showMessageDialog(this, "No se pueden editar los ID");
		} else if (Integer.valueOf(valorRecalulado) < 0 ) {
			JOptionPane.showMessageDialog(this, "La fecha de checkin no puede ser superior a la fecha de checkout");			
		} else	{
			this.reservasController.modificarReserva(Date.valueOf(fE), Date.valueOf(fS), valor, formaPago, id);
			
			JOptionPane.showMessageDialog(this, String.format("Registro modificado con exito para la reserva: " + id));	
		}
	});
}


private void modificarHuesped() {
	Optional.ofNullable(modeloHuesped.getValueAt(tbHuespedes.getSelectedRow(), tbHuespedes.getSelectedColumn()))
	.ifPresent(fila ->{
		int id = Integer.valueOf(modeloHuesped.getValueAt(tbHuespedes.getSelectedRow(), 0).toString());
		String nombre = (String) modeloHuesped.getValueAt(tbHuespedes.getSelectedRow(), 1).toString();
		String apellido = (String) modeloHuesped.getValueAt(tbHuespedes.getSelectedRow(), 2).toString();
		Date fechaNacimiento = Date.valueOf(modeloHuesped.getValueAt(tbHuespedes.getSelectedRow(), 3).toString());
		String nacionalidad = (String) modeloHuesped.getValueAt(tbHuespedes.getSelectedRow(), 4).toString();
		String telefono = (String) modeloHuesped.getValueAt(tbHuespedes.getSelectedRow(), 5).toString();
		int id_reserva = Integer.valueOf(modeloHuesped.getValueAt(tbHuespedes.getSelectedRow(), 6).toString());
		
		if (tbHuespedes.getSelectedColumn() == 0 || tbHuespedes.getSelectedColumn() == 6) {
			JOptionPane.showMessageDialog(this, "No se pueden editar los ID");
		} else {
		this.huespedesController.modificarHuesped(id, nombre, apellido, fechaNacimiento, nacionalidad, telefono, id_reserva);
		JOptionPane.showMessageDialog(this, String.format("Registro Id " + id + " huesped modificado con exito!"));	
		}
	});
	
}

private String recalulaValor(LocalDate fE, LocalDate fS) {
	if(fE!=null || fS!=null) {
		int dias = (int) ChronoUnit.DAYS.between(fE, fS);
		int estadia = 7500;
		int valor = dias*estadia;
		return Integer.toString(valor);
		
	}
	return "";
}


private void cargaTblHuespedes()  {
	
	List<Huespedes> huesplist = this.huespedesController.listarHuespedes();
	huesplist.forEach(h-> modeloHuesped.addRow(
			new Object[] {h.getId(),
							h.getNombre(),
							h.getApellido(),
							h.getFechaNacimiento(),
							h.getNacionalidad(),
							h.getTelefono(),
							h.getId_reserva()}));
}

private void buscarHuespedesApellido() {
	if(!txtBuscar.getText().equals("")) {
	
	List<Huespedes> huesplist = this.huespedesController.listarHuespedesApellido(txtBuscar.getText());
	if (huesplist.isEmpty()) {
		JOptionPane.showMessageDialog(this, "No existen huespedes con este apellido!", "WARNING", JOptionPane.WARNING_MESSAGE);
		cargaTblHuespedes();
		limpaBuscador();
	} else {
	huesplist.forEach(h-> modeloHuesped.addRow(
			new Object[] {h.getId(),
							h.getNombre(),
							h.getApellido(),
							h.getFechaNacimiento(),
							h.getNacionalidad(),
							h.getTelefono(),
							h.getId_reserva()}));
		}
	} else {
		JOptionPane.showMessageDialog(this, "Ingrese un criterio de busqueda valido!", "WARNING", JOptionPane.WARNING_MESSAGE);
		cargaTblHuespedes();
		limpaBuscador();
	}
}

public static boolean isNumeric(String str) { 
    try {  
      Double.parseDouble(str);  
      return true;
    } catch(NumberFormatException e){  
      return false;  
    }  
  }

protected void limpiarTblHuesped() {
	modeloHuesped.getDataVector().clear();
}

protected void limpiarTblReserva() {
	modelo.getDataVector().clear();
}
private void limpaBuscador() {
	txtBuscar.setText("");
}


//Código que permite mover la ventana por la pantalla según la posición de "x" y "y"
	 private void headerMousePressed(java.awt.event.MouseEvent evt) {
	        xMouse = evt.getX();
	        yMouse = evt.getY();
	    }

	    private void headerMouseDragged(java.awt.event.MouseEvent evt) {
	        int x = evt.getXOnScreen();
	        int y = evt.getYOnScreen();
	        this.setLocation(x - xMouse, y - yMouse);
}
}
