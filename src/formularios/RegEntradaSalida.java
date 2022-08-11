package formularios;

import BD.ConexionBD;
import com.mysql.cj.protocol.Resultset;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;

public class RegEntradaSalida extends javax.swing.JFrame {

    private String cedula = "", nombre = "";
    private String jornadaMañana[] = null, jornadaTarde[] = null;
    private final DefaultTableModel modelo;

    public RegEntradaSalida(String cedula) {
        initComponents();
        setTitle("Asistencia");
        setSize(1155, 651);
        setLocationRelativeTo(null);
        this.cedula = cedula;
        txt_cedula.setText(cedula);
        javax.swing.Timer t = new javax.swing.Timer(1000, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Date fecha = new Date();
                SimpleDateFormat dt = new SimpleDateFormat("E, dd/MM/yyyy");
                String fechaa = dt.format(fecha);
                FechaActual.setText(fechaa);
                dt = new SimpleDateFormat("HH:mm:ss");
                String hora = dt.format(fecha);
                HoraActual.setText(hora);
            }
        });

        t.start();

        try {
            Connection cn = ConexionBD.conectar();
            PreparedStatement ps = cn.prepareStatement("select nombre, jornada_manana, jornada_tarde from docentes where cedula = '" + cedula + "'");
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                this.nombre = rs.getString("nombre");
                this.jornadaMañana = rs.getString("jornada_manana").split("-");
                this.jornadaTarde = rs.getString("jornada_tarde").split("-");
            }

            System.out.println(jornadaMañana[0] + "  " + jornadaMañana[1] + "  " + jornadaTarde[0] + "  " + jornadaTarde[1]);
            javax.swing.Timer t2 = new javax.swing.Timer(1000, new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    Date fecha = new Date();
                    SimpleDateFormat dt = new SimpleDateFormat("HH:mm");
                    String hora = dt.format(fecha);
                    LocalTime horaActual = LocalTime.parse(hora);
                    boolean validar = false;

                    // if(horaActual.compareTo(LocalTime.parse("06:50")) >= 0 && horaActual.compareTo(LocalTime.parse("13:00")) < 0){                  
                    // Reemplazar por la hora de la BD
                    if (horaActual.compareTo(LocalTime.parse(jornadaMañana[0]).minusMinutes(10)) >= 0 && horaActual.compareTo(LocalTime.parse(jornadaMañana[1])) < 0) {

                        txt_entrada.setText(jornadaMañana[0]);
                        txt_salida.setText(jornadaMañana[1]);
                    }

                    if (horaActual.compareTo(LocalTime.parse(jornadaTarde[0])) >= 0 && horaActual.compareTo(LocalTime.parse(jornadaTarde[1])) <= 0) {
                        // Reemplazar por la hora de la BD
                        txt_entrada.setText(jornadaTarde[0]);
                        txt_salida.setText(jornadaTarde[1]);
                    }

                    if (horaActual.compareTo(LocalTime.parse("13:00")) > 0 && horaActual.compareTo(LocalTime.parse("14:00")) <= 0) {
                        btn_registrarEntradaSalida.setEnabled(false);
                    }

                    //JORNADA DE LA MAÑANA
                    //validando que pique 10 y 15 minutos antes  --  Cambiar la hora estatica por la de la bd 
                    // if (horaActual.compareTo(LocalTime.parse("06:50")) >= 0 && horaActual.compareTo(LocalTime.parse("07:15")) <= 0) {
                    if (horaActual.compareTo(LocalTime.parse(jornadaMañana[0]).minusMinutes(10)) >= 0 && horaActual.compareTo(LocalTime.parse(jornadaMañana[0]).plusMinutes(15)) <= 0) {
                        btn_registrarEntradaSalida.setEnabled(true);
                        btn_registrarEntradaSalida.setText("Registrar Entrada");
                        validar = true;
                    }
                    // Cambiar la hora estatica por la de la bd 
                    //if(horaActual.compareTo(LocalTime.parse("12:55")) >= 0 && horaActual.compareTo(LocalTime.parse("13:00")) <= 0){
                    if (horaActual.compareTo(LocalTime.parse(jornadaMañana[1]).minusMinutes(5)) >= 0 && horaActual.compareTo(LocalTime.parse(jornadaMañana[1]).plusMinutes(10)) <= 0) {
                        btn_registrarEntradaSalida.setEnabled(true);
                        btn_registrarEntradaSalida.setText("Registrar Salida");
                        validar = true;
                    }

                    //JORNADA DE LA TARDE
                    //validando que pique 10 y 15 minutos antes  --  Cambiar la hora estatica por la de la bd 
                    // if (horaActual.compareTo(LocalTime.parse("14:00")) > 0 && horaActual.compareTo(LocalTime.parse("14:15")) <= 0) {
                    if (horaActual.compareTo(LocalTime.parse(jornadaTarde[0]).minusMinutes(10)) > 0 && horaActual.compareTo(LocalTime.parse(jornadaTarde[0]).plusMinutes(15)) <= 0) {
                        btn_registrarEntradaSalida.setEnabled(true);
                        btn_registrarEntradaSalida.setText("Registrar Entrada");
                        validar = true;
                    }
                    // Cambiar la hora estatica por la de la bd 
                    //if(horaActual.compareTo(LocalTime.parse("16:55")) >= 0 && horaActual.compareTo(LocalTime.parse("17:15")) <= 0){

                    if (horaActual.compareTo(LocalTime.parse(jornadaTarde[1]).minusMinutes(5)) >= 0 && horaActual.compareTo(LocalTime.parse(jornadaTarde[1]).plusMinutes(10)) <= 0) {
                        btn_registrarEntradaSalida.setEnabled(true);
                        btn_registrarEntradaSalida.setText("Registrar Salida");
                        validar = true;
                    }

                    txt_tipo.setText(btn_registrarEntradaSalida.getText().substring(10, btn_registrarEntradaSalida.getText().length()));

                    //validando que no se pueda picar pasado el termino de la jornada laborarl
                    //if(horaActual.compareTo(LocalTime.parse("17:15")) >= 0){
                    if (horaActual.compareTo(LocalTime.parse(jornadaTarde[1]).plusMinutes(15)) >= 0) {
                        btn_registrarEntradaSalida.setEnabled(false);
                        btn_registrarEntradaSalida.setText("Fin Jornada");
                        txt_entrada.setText("Fin Jornada");
                        txt_salida.setText("Fin Jornada");
                        txt_tipo.setText("Fin Jornada");
                    }

                    if (!validar) {
                        btn_registrarEntradaSalida.setEnabled(false);
                    }
                }
            });
            t2.start();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }

        //LLENANDO TABLA
        modelo = new DefaultTableModel();
        this.tabla.setModel(modelo);
        jScrollPane1.setViewportView(this.tabla);
        modelo.addColumn("Día");
        modelo.addColumn("Cedula");
        modelo.addColumn("Nombre");
        modelo.addColumn("Inicio Jornada");
        modelo.addColumn("Fin Jornada");
        modelo.addColumn("Nro. Jornada");
        Date fecha = new Date();
        SimpleDateFormat dt = new SimpleDateFormat("E,");
        String dia = dt.format(fecha);
        String horaInicioJ = jornadaMañana[0], horaFinJ = jornadaMañana[1], horaInicioJ2 = jornadaTarde[0], horaFinJ2 = jornadaTarde[1];

        String[] fila1 = new String[6];
        fila1[0] = dia;
        fila1[1] = this.cedula;
        fila1[2] = this.nombre;
        fila1[3] = horaInicioJ;
        fila1[4] = horaFinJ;
        fila1[5] = "Jornada 1";

        String[] fila2 = new String[6];
        fila2[0] = dia;
        fila2[1] = this.cedula;
        fila2[2] = this.nombre;
        fila2[3] = horaInicioJ2;
        fila2[4] = horaFinJ2;
        fila2[5] = "Jornada 2";
        modelo.addRow(fila1);
        modelo.addRow(fila2);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel2 = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        FechaActual = new javax.swing.JFormattedTextField();
        HoraActual = new javax.swing.JFormattedTextField();
        btn_registrarEntradaSalida = new javax.swing.JButton();
        btn_cancelar = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        txt_salida = new javax.swing.JTextField();
        txt_cedula = new javax.swing.JTextField();
        txt_tipo = new javax.swing.JTextField();
        txt_entrada = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tabla = new javax.swing.JTable();
        btn_reporte = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(51, 153, 255));
        setForeground(javax.swing.UIManager.getDefaults().getColor("textHighlight"));
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));
        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 2));
        jPanel1.setOpaque(false);

        FechaActual.setEditable(false);
        FechaActual.setBackground(new java.awt.Color(255, 255, 255));
        FechaActual.setBorder(null);
        FechaActual.setForeground(new java.awt.Color(0, 0, 0));
        FechaActual.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.DateFormatter(new java.text.SimpleDateFormat("dd/MM/yyyy"))));
        FechaActual.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        FechaActual.setFont(new java.awt.Font("Arial", 0, 24)); // NOI18N

        HoraActual.setEditable(false);
        HoraActual.setBackground(new java.awt.Color(255, 255, 255));
        HoraActual.setBorder(null);
        HoraActual.setForeground(new java.awt.Color(0, 0, 0));
        HoraActual.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        HoraActual.setFont(new java.awt.Font("Arial", 1, 48)); // NOI18N

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(HoraActual, javax.swing.GroupLayout.DEFAULT_SIZE, 536, Short.MAX_VALUE)
            .addComponent(FechaActual)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(FechaActual, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(HoraActual, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(30, Short.MAX_VALUE))
        );

        jPanel2.add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 20, 540, 160));

        btn_registrarEntradaSalida.setBackground(new java.awt.Color(51, 215, 29));
        btn_registrarEntradaSalida.setForeground(new java.awt.Color(255, 255, 255));
        btn_registrarEntradaSalida.setText("Registrar Entrada");
        btn_registrarEntradaSalida.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        btn_registrarEntradaSalida.setBorderPainted(false);
        btn_registrarEntradaSalida.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btn_registrarEntradaSalida.setFocusPainted(false);
        btn_registrarEntradaSalida.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_registrarEntradaSalidaActionPerformed(evt);
            }
        });
        jPanel2.add(btn_registrarEntradaSalida, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 210, 190, 40));

        btn_cancelar.setBackground(new java.awt.Color(225, 0, 23));
        btn_cancelar.setForeground(new java.awt.Color(255, 255, 255));
        btn_cancelar.setText("Cancelar");
        btn_cancelar.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        btn_cancelar.setBorderPainted(false);
        btn_cancelar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btn_cancelar.setFocusPainted(false);
        btn_cancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_cancelarActionPerformed(evt);
            }
        });
        jPanel2.add(btn_cancelar, new org.netbeans.lib.awtextra.AbsoluteConstraints(590, 210, 190, 40));

        jLabel1.setBackground(new java.awt.Color(255, 153, 153));
        jLabel1.setForeground(new java.awt.Color(0, 0, 0));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Salida/Jornada");
        jLabel1.setOpaque(true);
        jPanel2.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(710, 300, 110, 30));

        jLabel2.setBackground(new java.awt.Color(255, 153, 153));
        jLabel2.setForeground(new java.awt.Color(0, 0, 0));
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText("Cédula");
        jLabel2.setOpaque(true);
        jPanel2.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 300, 110, 30));

        jLabel3.setBackground(new java.awt.Color(255, 153, 153));
        jLabel3.setForeground(new java.awt.Color(0, 0, 0));
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel3.setText("Tipo Registro");
        jLabel3.setOpaque(true);
        jPanel2.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(470, 300, 110, 30));

        jLabel4.setBackground(new java.awt.Color(255, 153, 153));
        jLabel4.setForeground(new java.awt.Color(0, 0, 0));
        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel4.setText("Entrada/Jornada");
        jLabel4.setOpaque(true);
        jPanel2.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(590, 300, 110, 30));

        txt_salida.setEditable(false);
        txt_salida.setBackground(new java.awt.Color(255, 255, 255));
        txt_salida.setForeground(new java.awt.Color(0, 0, 0));
        txt_salida.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txt_salida.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanel2.add(txt_salida, new org.netbeans.lib.awtextra.AbsoluteConstraints(710, 340, 110, 30));

        txt_cedula.setEditable(false);
        txt_cedula.setBackground(new java.awt.Color(255, 255, 255));
        txt_cedula.setForeground(new java.awt.Color(0, 0, 0));
        txt_cedula.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txt_cedula.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanel2.add(txt_cedula, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 340, 110, 30));

        txt_tipo.setEditable(false);
        txt_tipo.setBackground(new java.awt.Color(255, 255, 255));
        txt_tipo.setForeground(new java.awt.Color(0, 0, 0));
        txt_tipo.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txt_tipo.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanel2.add(txt_tipo, new org.netbeans.lib.awtextra.AbsoluteConstraints(470, 340, 110, 30));

        txt_entrada.setEditable(false);
        txt_entrada.setBackground(new java.awt.Color(255, 255, 255));
        txt_entrada.setForeground(new java.awt.Color(0, 0, 0));
        txt_entrada.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txt_entrada.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanel2.add(txt_entrada, new org.netbeans.lib.awtextra.AbsoluteConstraints(590, 340, 110, 30));

        jLabel5.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(0, 0, 0));
        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel5.setText("Registro De Asistencia");
        jPanel2.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(306, 410, 540, -1));

        tabla.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "Día", "Cédula", "Nombre", "Hora Inicio Jornada", "Hora Fin Jornada", "Nro. Jornada"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(tabla);

        jPanel2.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 450, 770, 90));

        btn_reporte.setBackground(new java.awt.Color(0, 0, 0));
        btn_reporte.setForeground(new java.awt.Color(255, 255, 255));
        btn_reporte.setText("Generar Reporte");
        btn_reporte.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        btn_reporte.setBorderPainted(false);
        btn_reporte.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btn_reporte.setFocusPainted(false);
        btn_reporte.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_reporteActionPerformed(evt);
            }
        });
        jPanel2.add(btn_reporte, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 550, 130, 40));

        getContentPane().add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(-4, 1, 1160, 650));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btn_registrarEntradaSalidaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_registrarEntradaSalidaActionPerformed
        Date fecha = new Date();
        SimpleDateFormat dt = new SimpleDateFormat("dd/MM/yyyy");
        String fechaa = dt.format(fecha);
        dt = new SimpleDateFormat("HH:mm");
        String hora = dt.format(fecha);
        String tipo_jornada = "";
        String hora_bd = "";
        LocalTime horaActual = LocalTime.parse(hora);
        String tipo = btn_registrarEntradaSalida.getText().substring(10, btn_registrarEntradaSalida.getText().length()), jornada = txt_tipo.getText();

        if (horaActual.compareTo(LocalTime.parse("06:00")) >= 0 && horaActual.compareTo(LocalTime.parse("13:00")) <= 0) {
            tipo_jornada = "Mañana";
        } else {
            tipo_jornada = "Tarde";
        }

        try {

            Connection cn = ConexionBD.conectar();
            String sql = "Select hora from  reportes where cedula='" + this.cedula + "' and fecha='" + fechaa + "' and tipo='" + tipo + "' and jornada='" + tipo_jornada + "'";
            PreparedStatement psd = cn.prepareStatement(sql);
            ResultSet rs = psd.executeQuery();

            if (rs.next()) {
                hora_bd = rs.getString("hora");
                JOptionPane.showMessageDialog(this, "No puede volverse a registrar");
            } else {
                PreparedStatement ps = cn.prepareStatement("insert into reportes (ID, cedula, fecha, hora, tipo, jornada) values (?,?,?,?,?,?)");
                ps.setInt(1, 0);
                ps.setString(2, this.cedula);
                ps.setString(3, fechaa);
                ps.setString(4, hora);
                ps.setString(5, tipo);
                ps.setString(6, tipo_jornada);
                ps.executeUpdate();
                btn_registrarEntradaSalida.setEnabled(false);
                JOptionPane.showMessageDialog(this, "Registrado");
            }

        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        btn_registrarEntradaSalida.setEnabled(false);

    }//GEN-LAST:event_btn_registrarEntradaSalidaActionPerformed

    private void btn_reporteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_reporteActionPerformed
        try {
            Date fecha = new Date();
            SimpleDateFormat dt = new SimpleDateFormat("dd/MM/yyyy");
            String fechaa = dt.format(fecha);
            Connection cn = ConexionBD.conectar();
            Map parametros = new HashMap();
            parametros.put("fecha_buscar", fechaa);
            parametros.put("cedula_reporte", this.cedula);
            JasperDesign jdesign = JRXmlLoader.load("src/reports/reporte.jrxml");
            JasperReport report = JasperCompileManager.compileReport(jdesign);
            JasperPrint jprint = JasperFillManager.fillReport(report, parametros, cn);
            JasperViewer view = new JasperViewer(jprint, false);
            view.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
            view.setVisible(true);
        } catch (JRException ex) {
            System.err.println(ex.getMessage());
        }
    }//GEN-LAST:event_btn_reporteActionPerformed

    private void btn_cancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_cancelarActionPerformed
        Login login = new Login();
        login.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_btn_cancelarActionPerformed

    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new RegEntradaSalida("").setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JFormattedTextField FechaActual;
    private javax.swing.JFormattedTextField HoraActual;
    private javax.swing.JButton btn_cancelar;
    private javax.swing.JButton btn_registrarEntradaSalida;
    private javax.swing.JButton btn_reporte;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tabla;
    private javax.swing.JTextField txt_cedula;
    private javax.swing.JTextField txt_entrada;
    private javax.swing.JTextField txt_salida;
    private javax.swing.JTextField txt_tipo;
    // End of variables declaration//GEN-END:variables

}
