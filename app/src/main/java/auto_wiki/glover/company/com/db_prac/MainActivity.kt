package auto_wiki.glover.company.com.db_prac

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import com.google.firebase.database.*

class MainActivity : AppCompatActivity() {

    lateinit var editTextName: EditText
    lateinit var ratingBarName: RatingBar
    lateinit var buttonName: Button
    lateinit var ref: DatabaseReference
    lateinit var heroList: MutableList<Hero>
    lateinit var listView: ListView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        heroList = mutableListOf()
        ref = FirebaseDatabase.getInstance().getReference("heroes")

        editTextName = findViewById(R.id.editText)
        ratingBarName = findViewById(R.id.ratingBar)
        buttonName = findViewById(R.id.button2)
        listView = findViewById(R.id.listViewItem)

        buttonName.setOnClickListener {
            saveHero()
        }

        ref.addValueEventListener(object: ValueEventListener {
            override fun onCancelled(p0: DatabaseError?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onDataChange(p0: DataSnapshot?) {
                if(p0!!.exists()) {
                    heroList!!.clear()
                    for(h in p0.children) {
                        val hero = h.getValue(Hero::class.java)
                        heroList.add(hero!!)
                    }
                    val adapter = HeroAdapter(this@MainActivity, R.layout.heros, heroList)
                    listView.adapter = adapter
                }
            }
        })
    }

    fun saveHero() {
        val name = editTextName.text.toString().trim()

        if(name.isEmpty()) {
            editTextName.error = "Please eneter text!"
            return
        }

        val heroId = ref.push().key
        val hero = Hero(heroId, name, ratingBarName.rating.toInt())

        ref.child(heroId).setValue(hero).addOnCompleteListener {
            Toast.makeText(applicationContext, "Hero saved!", Toast.LENGTH_SHORT).show()
        }
    }
}
