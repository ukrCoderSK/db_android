package auto_wiki.glover.company.com.db_prac

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.google.firebase.database.FirebaseDatabase
import org.w3c.dom.Text

class HeroAdapter(val mCtx: Context, val layourResId: Int, val heroList: List<Hero>)
    : ArrayAdapter<Hero>(mCtx, layourResId, heroList) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val layoutInflater: LayoutInflater = LayoutInflater.from(mCtx)
        val view: View = layoutInflater.inflate(layourResId, null)
        val textView = view.findViewById<TextView>(R.id.textView)
        val textViewUpdate = view.findViewById<TextView>(R.id.textViewUpdate)
        val hero = heroList[position]
        textView.text = hero.name
        textViewUpdate.setOnClickListener {
            showUpdateDialog(hero)
        }
        return view
    }

    fun showUpdateDialog(hero: Hero) {
        val builder = AlertDialog.Builder(mCtx)
        builder.setTitle("Update")
        val inflater = LayoutInflater.from(mCtx)
        val view = inflater.inflate(R.layout.layout_update_herous, null)
        val editText = view.findViewById<EditText>(R.id.editText)
        val ratingBar = view.findViewById<RatingBar>(R.id.ratingBar)

        editText.setText(hero.name)
        ratingBar.rating = hero.raiting.toFloat()

        builder.setView(view)
        builder.setPositiveButton("Update") { dialog, which ->
            val dbHero = FirebaseDatabase.getInstance().getReference("heroes")
            val name = editText.text.toString().trim()
            if(name.isEmpty()) {
                editText.error = "Enter name!"
                editText.requestFocus();
                return@setPositiveButton
            }

            val hero = Hero(hero.id, name, ratingBar.rating.toInt())
            dbHero.child(hero.id).setValue(hero)
            Toast.makeText(mCtx, "Hero updating!", Toast.LENGTH_SHORT).show()
        }
        builder.setNegativeButton("No") { dialog, which ->

        }

        val alert = builder.create()
        alert.show()
    }
}