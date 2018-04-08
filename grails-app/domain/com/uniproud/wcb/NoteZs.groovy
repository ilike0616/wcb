package com.uniproud.wcb

class NoteZs {
	static belongsTo = [Note,Employee]
	/**
	 * 部门
	 */
	Dept dept
	/**
	 * 随笔
	 */
	Note note
	/**
	 * 点赞人
	 */
	Employee employee

    static constraints = {
		note nullable:false
		employee nullable:false
    }
	
	static mapping = {
		table('t_note_zs')
	}
}
