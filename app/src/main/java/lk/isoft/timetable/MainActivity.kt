package lk.isoft.timetable

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View.inflate
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.resources.Compatibility.Api21Impl.inflate
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.io.BufferedReader
import java.io.File
import java.nio.file.attribute.AclEntry
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.Year
import java.time.YearMonth
import java.util.*

class MainActivity : AppCompatActivity(), RecyclerAdapter.OnItemClickListener {
    var cy = 0
    var currmno = 0

    //class MainActivity : AppCompatActivity() {
    companion object {
        var days = mutableListOf<Int>(5)
        var daysofweek = mutableListOf<String>("")
        var indtoday = mutableListOf<String>("")
        var message = mutableListOf<String>("")
        //var days1 = mutableListOf<String>("Aaa","Bbb","Ccc")
    }

    val monthlist = arrayListOf<String>(
        "JANUARY",
        "FEBRUARY",
        "MARCH",
        "APRIL",
        "MAY",
        "JUNE",
        "JULY",
        "AUGUST",
        "SEPTEMBER",
        "OCTOBER",
        "NOVEMBER",
        "DECEMBER"
    )
    private var layoutManager: RecyclerView.LayoutManager? = null
    private var adapter: RecyclerView.Adapter<RecyclerAdapter.ViewHolder>? = null

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {

        val path: String = this.getFilesDir().toString() + "/User files"
        if (!File(path).exists()) {
            File(this.getFilesDir().toString(), "User files").mkdir()
        }
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        if (getSupportActionBar() != null) {
            getSupportActionBar()?.hide()
        }
        val current = LocalDateTime.now()
        cy = current.year.toInt()
        val curr = findViewById<TextView>(R.id.present)
        val bup = findViewById<Button>(R.id.imageButton)
        val bdown = findViewById<Button>(R.id.imageButton2)
        val currentmonth = current.month.toString()
        val currday = current.dayOfMonth
        var mthno = (monthlist.indexOf(currentmonth) + 1).toInt()
        currmno = mthno
        //days.removeAt(0)
        val jumpto = findViewById<Button>(R.id.button)
        val ccc = Calendar.getInstance()


        if (mthno - 1 == 0) {
            bup.text = "↑" + (cy - 1).toString() + " " + getMonth(12)
            bdown.text = "↓" + (cy).toString() + " " + getMonth(mthno + 1)
        } else if (mthno + 1 == 13) {
            bup.text = "↑" + (cy).toString() + " " + getMonth(mthno - 1)
            bdown.text = "↓" + (cy + 1).toString() + " " + getMonth(1)

        } else {
            bup.text = "↑" + (cy).toString() + " " + getMonth(mthno - 1)
            bdown.text = "↓" + (cy).toString() + " " + getMonth(mthno + 1)

        }
        curr.text = cy.toString() + " " + getMonth(currmno)
        ccc.set(cy, currmno - 1, 5)
        val noofdays = ccc.getActualMaximum(Calendar.DAY_OF_MONTH)
        Log.d(getMonth(currmno), noofdays.toString())
        Log.d("Day", ccc.get(Calendar.DAY_OF_WEEK).toString())
        days.clear()
        daysofweek.clear()
        indtoday.clear()
        message.clear()
        for (currentdate in 1..noofdays) {
            daysofweek.add(getDayOfWeek(cy, currmno, currentdate))
            days.add(currentdate)
            if (currentdate == currday) {
                indtoday.add("·")
            } else {
                indtoday.add("")
            }

            val consideringyear = cy.toString()
            val consideringmonth = currmno.toString()
            val path: String = this.getFilesDir()
                .toString() + "/User files/" + consideringyear + "." + consideringmonth
            if (File(path).exists()) {
                if (File(path + "/$currentdate").exists()) {
                    message.add(
                        getMessage(
                            consideringyear,
                            consideringmonth,
                            currentdate.toString()
                        )
                    )
                    Log.d(
                        "Deb",
                        getMessage(consideringyear, consideringmonth, currentdate.toString())
                    )
                } else {
                    message.add("")
                }

            } else {
                message.add("")


            }

        }
        Log.d("testarray", message.toString())
        layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager
        adapter = RecyclerAdapter(this)
        recyclerView.adapter = adapter
        println(days)


        jumpto.setOnClickListener {
            val dpview = LayoutInflater.from(this).inflate(R.layout.datepicker, null)
            val builder = AlertDialog.Builder(this).setView(dpview).setTitle("Please choose a date")
            val dshow = builder.show()
            val okb = dpview.findViewById<Button>(R.id.buttokdate)
            val cab = dpview.findViewById<Button>(R.id.canceldp)
            cab.setOnClickListener {
                dshow.dismiss()
            }
            okb.setOnClickListener {
                val datePicker = dpview.findViewById<DatePicker>(R.id.date_Picker)
                dshow.dismiss()
                Log.d("AAA", datePicker.year.toString())
                mthno = datePicker.month + 1
                currmno = mthno
                cy = datePicker.year
                if (mthno - 1 == 0) {
                    bup.text = "↑" + (cy - 1).toString() + " " + getMonth(12)
                    bdown.text = "↓" + (cy).toString() + " " + getMonth(mthno + 1)
                } else if (mthno + 1 == 13) {
                    bup.text = "↑" + (cy).toString() + " " + getMonth(mthno - 1)
                    bdown.text = "↓" + (cy + 1).toString() + " " + getMonth(1)

                } else {
                    bup.text = "↑" + (cy).toString() + " " + getMonth(mthno - 1)
                    bdown.text = "↓" + (cy).toString() + " " + getMonth(mthno + 1)

                }
                curr.text = cy.toString() + " " + getMonth(currmno)
                ccc.set(cy, currmno - 1, 6)
                val noofdays = ccc.getActualMaximum(Calendar.DAY_OF_MONTH)
                Log.d(getMonth(currmno), noofdays.toString())
                Log.d("Day", ccc.get(Calendar.DAY_OF_WEEK).toString())
                days.clear()
                daysofweek.clear()
                indtoday.clear()
                message.clear()
                for (currentdate in 1..noofdays) {
                    daysofweek.add(getDayOfWeek(cy, currmno, currentdate))
                    days.add(currentdate)
                    val consideringyear = cy.toString()
                    val consideringmonth = currmno.toString()
                    val path: String = this.getFilesDir()
                        .toString() + "/User files/" + consideringyear + "." + consideringmonth
                    if (File(path).exists()) {
                        if (File(path + "/$currentdate").exists()) {
                            message.add(
                                getMessage(
                                    consideringyear,
                                    consideringmonth,
                                    currentdate.toString()
                                )
                            )
                            Log.d(
                                "Deb",
                                getMessage(
                                    consideringyear,
                                    consideringmonth,
                                    currentdate.toString()
                                )
                            )
                        } else {
                            message.add("")
                        }

                    } else {
                        message.add("")


                    }
                }

                if (mthno == (monthlist.indexOf(currentmonth) + 1) && cy == current.year) {
                    for (currentdate in 1..noofdays) {

                        if (currentdate == currday) {
                            indtoday.add("·")
                        } else {
                            indtoday.add("")
                        }

                    }
                } else {
                    for (currentdate in 1..noofdays) {

                        indtoday.add("")
                    }
                }

                layoutManager = LinearLayoutManager(this)
                recyclerView.layoutManager = layoutManager
                adapter = RecyclerAdapter(this)
                recyclerView.adapter = adapter
            }
        }

        bup.setOnClickListener {
            mthno = mthno - 1
            //Log.d("Date", currday)

            if (mthno - 1 == 0) {
                bup.text = "↑" + (cy - 1).toString() + " " + getMonth(12)
                bdown.text = "↓" + (cy).toString() + " " + getMonth(2)
                currmno = 1

            } else if (mthno - 1 == -1) {
                cy = cy - 1
                bup.text = "↑" + (cy).toString() + " " + getMonth(11)
                bdown.text = "↓" + (cy + 1).toString() + " " + getMonth(1)
                mthno = 12
                currmno = 12

            } else {
                bup.text = "↑" + (cy).toString() + " " + getMonth(mthno - 1)
                bdown.text = "↓" + (cy).toString() + " " + getMonth(mthno + 1)
                currmno = mthno
            }
            curr.text = cy.toString() + " " + getMonth(currmno)
            ccc.set(cy, currmno - 1, 6)
            val noofdays = ccc.getActualMaximum(Calendar.DAY_OF_MONTH)
            Log.d(getMonth(currmno), noofdays.toString())
            message.clear()
            days.clear()
            daysofweek.clear()
            indtoday.clear()
            for (currentdate in 1..noofdays) {
                daysofweek.add(getDayOfWeek(cy, currmno, currentdate))
                days.add(currentdate)
                val consideringyear = cy.toString()
                val consideringmonth = currmno.toString()
                val path: String = this.getFilesDir()
                    .toString() + "/User files/" + consideringyear + "." + consideringmonth
                if (File(path).exists()) {
                    if (File(path + "/$currentdate").exists()) {
                        message.add(
                            getMessage(
                                consideringyear,
                                consideringmonth,
                                currentdate.toString()
                            )
                        )
                        //Log.d("Deb",getMessage(consideringyear,consideringmonth,currentdate.toString()))
                    } else {
                        message.add("")
                    }

                } else {
                    message.add("")


                }
            }
            if (mthno == (monthlist.indexOf(currentmonth) + 1) && cy == current.year) {
                for (currentdate in 1..noofdays) {

                    if (currentdate == currday) {
                        indtoday.add("·")
                    } else {
                        indtoday.add("")
                    }

                }
            } else {
                for (currentdate in 1..noofdays) {

                    indtoday.add("")
                }
            }
            layoutManager = LinearLayoutManager(this)
            recyclerView.layoutManager = layoutManager
            adapter = RecyclerAdapter(this)
            recyclerView.adapter = adapter

        }
        bdown.setOnClickListener {
            mthno = mthno + 1
            if (mthno + 1 == 13) {
                bup.text = "↑" + (cy).toString() + " " + getMonth(mthno - 1)
                bdown.text = "↓" + (cy + 1).toString() + " " + getMonth(1)
                currmno = 12

            } else if (mthno + 1 == 14) {
                cy = cy + 1
                bup.text = "↑" + (cy - 1).toString() + " " + getMonth(12)
                bdown.text = "↓" + (cy).toString() + " " + getMonth(2)
                mthno = 1
                currmno = 1
            } else {
                bup.text = "↑" + (cy).toString() + " " + getMonth(mthno - 1)
                bdown.text = "↓" + (cy).toString() + " " + getMonth(mthno + 1)
                currmno = mthno
            }
            curr.text = cy.toString() + " " + getMonth(currmno)
            ccc.set(cy, currmno - 1, 6)
            val noofdays = ccc.getActualMaximum(Calendar.DAY_OF_MONTH)
            Log.d(getMonth(currmno), noofdays.toString())
            days.clear()
            daysofweek.clear()
            indtoday.clear()
            message.clear()
            for (currentdate in 1..noofdays) {
                daysofweek.add(getDayOfWeek(cy, currmno, currentdate))
                days.add(currentdate)

                val consideringyear = cy.toString()
                val consideringmonth = currmno.toString()
                val path: String = this.getFilesDir()
                    .toString() + "/User files/" + consideringyear + "." + consideringmonth
                if (File(path).exists()) {
                    if (File(path + "/$currentdate").exists()) {
                        message.add(
                            getMessage(
                                consideringyear,
                                consideringmonth,
                                currentdate.toString()
                            )
                        )
                        Log.d(
                            "Deb",
                            getMessage(consideringyear, consideringmonth, currentdate.toString())
                        )
                    } else {
                        message.add("")
                    }

                } else {
                    message.add("")


                }
            }
            if (mthno == (monthlist.indexOf(currentmonth) + 1) && cy == current.year) {
                for (currentdate in 1..noofdays) {

                    if (currentdate == currday) {
                        indtoday.add("·")
                    } else {
                        indtoday.add("")
                    }

                }
            } else {
                for (currentdate in 1..noofdays) {

                    indtoday.add("")
                }
            }

            layoutManager = LinearLayoutManager(this)
            recyclerView.layoutManager = layoutManager
            adapter = RecyclerAdapter(this)
            recyclerView.adapter = adapter

        }

    }


    fun getMonth(mnthno: Int): String {

        return monthlist[mnthno - 1]
    }

    override fun OnItemClick(position: Int) {
        //Toast.makeText(this, position.toString(), Toast.LENGTH_SHORT).show()
        if (message[position] == "") {

            val mdview = LayoutInflater.from(this).inflate(R.layout.neweventdialog, null)
            val mbuilder = AlertDialog.Builder(this).setView(mdview).setTitle("Create a new event")
            val dshow = mbuilder.show()
            val intext = mdview.findViewById<EditText>(R.id.intext)
            val datec = mdview.findViewById<TextView>(R.id.date)
            val d = days[position]
            val datef = cy.toString() + "." + currmno.zeroFormatter() + "." + d.zeroFormatter()
            datec.text = datef
            val cabut = mdview.findViewById<Button>(R.id.canb)
            val okbut = mdview.findViewById<Button>(R.id.okb)
            mdview.findViewById<TextView>(R.id.important).setTextSize(0F)
            mdview.findViewById<TextView>(R.id.mess).setTextSize(0F)
            mdview.findViewById<TextView>(R.id.important).text = ""
            mdview.findViewById<TextView>(R.id.mess).text = ""
            cabut.setOnClickListener {
                dshow.dismiss()
            }

            val consideringyear = cy.toString()
            val consideringmonth = currmno.toString()
            val fname = d.toString()

            val path: String = this.getFilesDir()
                .toString() + "/User files/" + consideringyear + "." + consideringmonth
            if (!File(path).exists()) {
                File(
                    this.getFilesDir().toString() + "/User files",
                    consideringyear + "." + consideringmonth
                ).mkdir()
            }
            okbut.setOnClickListener {

                if (intext.text.toString() == "") {
                    dshow.dismiss()
                } else {
                    dshow.dismiss()
                    if (savedata(
                            fname,
                            intext.text.toString(),
                            consideringyear,
                            consideringmonth
                        )
                    ) {
                        Toast.makeText(this, "Event saved successfully", Toast.LENGTH_SHORT).show()
                        message.set(position, intext.text.toString())
                        adapter?.notifyItemChanged(position)
                    } else {
                        Toast.makeText(this, "Whatever fuck happened mate", Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            }
        }
        else {
            val d = days[position]
            val datef = cy.toString() + "." + currmno.zeroFormatter() + "." + d.zeroFormatter()
            val messageview = LayoutInflater.from(this).inflate(R.layout.message_viewer,null)

            val messagebuild = AlertDialog.Builder(this).setView(messageview).setTitle(datef)
            val mdshow = messagebuild.show()
            val messshow = messageview.findViewById<TextView>(R.id.message)

            messshow.text = message[position]
            val butedit = messageview.findViewById<Button>(R.id.editbut)
            val butok = messageview.findViewById<Button>(R.id.okbut)
            butok.setOnClickListener{
                mdshow.dismiss()
            }
            butedit.setOnClickListener{
                val mdview = LayoutInflater.from(this).inflate(R.layout.neweventdialog, null)
                val mbuilder = AlertDialog.Builder(this).setView(mdview).setTitle("Edit event")
                val dshow = mbuilder.show()
                val intext = mdview.findViewById<EditText>(R.id.intext)
                val datec = mdview.findViewById<TextView>(R.id.date)
                mdview.findViewById<TextView>(R.id.important).text = "Important"
                mdview.findViewById<TextView>(R.id.mess).text = "Please note that leaving the above space blank and saving the event will remove the event from the database"
                intext.setText(message[position])
                intext.selectAll()
                val d = days[position]
                val datef = cy.toString() + "." + currmno.zeroFormatter() + "." + d.zeroFormatter()
                datec.text = datef
                val cabut = mdview.findViewById<Button>(R.id.canb)
                val okbut = mdview.findViewById<Button>(R.id.okb)
                cabut.setOnClickListener {
                    dshow.dismiss()
                    mdshow.dismiss()
                }

                val consideringyear = cy.toString()
                val consideringmonth = currmno.toString()
                val fname = d.toString()

                val path: String = this.getFilesDir()
                    .toString() + "/User files/" + consideringyear + "." + consideringmonth + "/$fname"

                okbut.setOnClickListener {

                    if (intext.text.toString() == "") {
                        dshow.dismiss()
                        mdshow.dismiss()
                        message.set(position, "")
                        File(path).delete()
                        adapter?.notifyItemChanged(position)

                    } else {
                        dshow.dismiss()
                        mdshow.dismiss()
                        File(path).delete()
                        if (savedata(
                                fname,
                                intext.text.toString(),
                                consideringyear,
                                consideringmonth
                            )
                        ) {
                            Toast.makeText(this, "Event saved successfully", Toast.LENGTH_SHORT).show()
                            message.set(position, intext.text.toString())
                            adapter?.notifyItemChanged(position)
                        } else {
                            Toast.makeText(this, "Whatever fuck happened mate", Toast.LENGTH_SHORT)
                                .show()
                        }
                    }
                }
            }
        }

    }

    fun savedata(
        fname: String,
        message: String,
        consideringyear: String,
        consideringmonth: String
    ): Boolean {
        var file = File(
            this.getFilesDir()
                .toString() + "/User files/" + consideringyear + "." + consideringmonth + "/" + fname
        )
        if (!file.createNewFile()) {
            return false
        } else {
            file.writeText(message)
            return true
        }


    }

    fun getMessage(Year: String, Month: String, Day: String): String {
        val path: String = this.getFilesDir().toString() + "/User files/" + Year + "." + Month + "/"
        val br: BufferedReader =
            File(path + Day).bufferedReader()
        val istr = br.use { it.readText() }

        //Log.d("Aaa",ele.contentToString())
        return istr
    }

    fun Int.zeroFormatter(): String {
        if (this < 10) {
            return "0" + this.toString()
        } else {
            return this.toString()
        }
    }

    fun getDayOfWeek(Y: Int, M: Int, D: Int): String {
        val c = Calendar.getInstance()
        c.set(Y, M - 1, D - 1)
        val daysofweek1 = arrayListOf<String>("MON", "TUE", "WED", "THU", "FRI", "SAT", "SUN")

        return daysofweek1[c.get(Calendar.DAY_OF_WEEK) - 1]
    }

}