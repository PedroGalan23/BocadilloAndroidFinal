package com.example.bocadilloandroidfinal._alumno

import android.graphics.Bitmap
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.bocadilloandroidfinal.R
import com.example.bocadilloandroidfinal.databinding.FragmentResumenPedidoBinding
import com.example.bocadilloandroidfinal.viewmodels.PedidoViewModel
import com.example.bocadilloandroidfinal.viewmodels.UsuarioViewModel
import com.google.zxing.BarcodeFormat
import com.google.zxing.qrcode.QRCodeWriter

class ResumenPedidoFragment : Fragment() {
    //Definimos el binding de manera segura para evitar problemas de fugas
    private var _binding: FragmentResumenPedidoBinding? = null
    private val binding get() = _binding!!


    //Definimos Los viewModels
    private val usuarioViewModel: UsuarioViewModel by viewModels()
    private val pedidoViewModel: PedidoViewModel by viewModels()

    //Creamos la vista a partir del binding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentResumenPedidoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //Observamos el usuario para obtener mediante una función lambda para evitar nulos el pedido del dia para ese id_alumno
        usuarioViewModel.usuarioAutenticado.observe(viewLifecycleOwner) { usuario ->
            usuario?.let {
                it.id?.let { it1 -> pedidoViewModel.obtenerPedidoDelDia(it1) }
            }
        }

        //Observamos el pedido del dia mediante una función lambda para recoger los valores
        pedidoViewModel.pedidoDelDia.observe(viewLifecycleOwner) { pedido ->
            if (pedido != null) {
                //Asignamos el valor de las variables
                binding.txtNombreBocadillo.text = pedido.bocadillo.nombre
                binding.txtDescripcionBocadillo.text = pedido.bocadillo.descripcion
                binding.txtCosteBocadillo.text = "${pedido.precio}€"

                //Asignamos el icono mediante la referencia guardada en FireBase
                val iconoId = resources.getIdentifier(
                    pedido.bocadillo.icono, "drawable", requireContext().packageName
                )
                if (iconoId != 0) {
                    binding.imgBocadillo.setImageResource(iconoId)
                }
                val idUnico = pedido.id
                val qrBitmap = generarQRConIdUnico(idUnico)
                if (qrBitmap != null) {
                    //Asignamos la imagen del qr
                    binding.qrImageView.setImageBitmap(qrBitmap)
                }

                //Cuando se pulsa en generar Pedido llamamos a cancelarPedido y navegamos
                binding.btnCancelarPedido.setOnClickListener {
                    pedidoViewModel.cancelarPedido()
                    findNavController().navigate(R.id.action_resumenFragment_to_fragment_pedido) // Ahora vuelve a PedidoFragment
                }
            }
        }
    }
    //Función para generar un qr único a partir de un id pedido
    fun generarQRConIdUnico(id: String): Bitmap? {
        try {
            val qrCodeWriter = QRCodeWriter()
            val bitMatrix = qrCodeWriter.encode(id, BarcodeFormat.QR_CODE, 512, 512)
            val width = bitMatrix.width
            val height = bitMatrix.height
            val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565)

            for (x in 0 until width) {
                for (y in 0 until height) {
                    bitmap.setPixel(x, y, if (bitMatrix[x, y]) android.graphics.Color.BLACK else android.graphics.Color.WHITE)
                }
            }
            return bitmap
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
